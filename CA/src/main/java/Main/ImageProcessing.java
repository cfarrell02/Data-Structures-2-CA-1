package Main;

import Models.ComponentGroup;
import Models.PixelGroup;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Comparator;


import static Utilities.Utilities.*;

/**
 * An Encompassing class that runs the image processing in a seperate thread to the JAVAFX UI
 */
public class ImageProcessing extends Task<Image> {
    private final int[] pixels;
    private final int width, height;
    private final Image image;
    private final double noiseFilter;
    private final Color color;
    private final String name;
    private final boolean newGroup;

    public ImageProcessing(Image image, int[] pixels, int width, int height, double noiseFilter, String name, Color color, boolean newGroup) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        this.image = image;
        this.noiseFilter = noiseFilter;
        this.name = name;
        this.color = color;
        this.newGroup = newGroup;
    }

    /**
     * This method provides the processing for the image. It groups together all nearby pixels into disjoint sets.
     * It then removes any groups less than the noisefilter size. It adds these groups to a component group and marks
     * all their extremities. It also adds in the corresponding array into the component group. This component group
     * is then saved to a hashmap in the image controller.
     * @return An image with boxes drawn around valid groups
     */
    @Override
    protected Image call() {
        updateProgress(0, 400);
        ComponentGroup cg = new ComponentGroup(name, color);
        cg.setSource(ImageController.x, ImageController.y);


        //groups together nearby pixels
        for (int i = 0; i < pixels.length; ++i) {
            if (pixels[i] != -1) {
                if (i % width != 0 && pixels[i - 1] != -1) pixels[i] = pixels[i - 1];
                else if (i % width != 0 && i / width != 0 && pixels[i - 1 - width] != -1)
                    pixels[i] = pixels[i - 1 - width];
                else if (i / width != 0 && pixels[i - width] != -1) pixels[i] = pixels[i - width];
                else if (i % width != width - 1 && i / width != 0 && pixels[i + 1 - width] != -1)
                    pixels[i] = pixels[i + 1 - width];
            }
        }
        updateProgress(100, 400);

        //Union Finds all groups nearby
        for (int i = 0; i < pixels.length; ++i) {

            if (pixels[i] != -1) {
                if (i / width != height - 1 && pixels[i + width] != -1 && findRoot(pixels, pixels[i + width]) != findRoot(pixels, pixels[i]))
                    pixels[findRoot(pixels, pixels[i + width])] = findRoot(pixels, pixels[i]);
                else if (i / width != height - 1 && i % width != width - 1 && pixels[i + width + 1] != -1 && findRoot(pixels, pixels[i + width + 1]) != findRoot(pixels, pixels[i]))
                    pixels[findRoot(pixels, pixels[i + width + 1])] = findRoot(pixels, pixels[i]);
                else if (i / width != height - 1 && i % width != 0 && pixels[i + width - 1] != -1 && findRoot(pixels, pixels[i + width - 1]) != findRoot(pixels, pixels[i]))
                    pixels[findRoot(pixels, pixels[i + width - 1])] = findRoot(pixels, pixels[i]);
            }
        }
        updateProgress(200, 400);

        //   Filter out noise by making root -1
        for (int i : getRoots(pixels)) {
            if (groupSize(pixels, i) < noiseFilter) {
                pixels[i] = -1;
            }
        }
        //Adds all pixel groups into the component group's array list
        for (int root : getRoots(pixels)) {
            if (root != -1)
                cg.addPixelGroup(new PixelGroup(groupSize(pixels, root), root));
        }
        updateProgress(250, 400);
        cg.getGroups().sort(Comparator.comparing(PixelGroup::getSize)); // ensures that groups are sized from smallest to largest

        // Marks the extremities of each group
        for (PixelGroup group : cg.getGroups()) {
            int topRow = height - 1, leftCol = width - 1, rightCol = 0, bottomRow = 0;
            for (int i = 0; i < pixels.length; ++i) {
                if (pixels[i] != -1 && findRoot(pixels, pixels[i]) == group.getRoot()) {
                    if (i / width < topRow) topRow = i / width;
                    if (i / width > bottomRow) bottomRow = i / width;
                    if (i % width < leftCol) leftCol = i % width;
                    if (i % width > rightCol) rightCol = i % width;
                }
            }
            group.setBounds(topRow, bottomRow, leftCol, rightCol);
        }
        // Adds the group to the components if it's new
        if (!newGroup)
            ImageController.components.remove(cg.getName());
        cg.setGroupPixels(pixels);
        ImageController.components.put(cg.getName(), cg);

        updateProgress(300, 400);

        ImageController.pixels = pixels;
        return markImage(cg, image, 1);
    }
    //row = index/width
    //col = index%width

}