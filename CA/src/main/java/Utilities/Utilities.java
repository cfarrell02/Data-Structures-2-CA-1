package Utilities;

import Models.ComponentGroup;
import Models.PixelGroup;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Utilities {



    /**
     * Checks if the provided int index provides a valid object from the provided array
     * @param index an index to be checked if valid
     * @param list an array for the index to be check in
     * @return true if the index is valid
     */
    public static boolean validIndex(int index, List list) {
        return ((index >= 0) && (index < list.size()));
    }


    /**
     * Checks if the third parameter is within the range of the first two paramters
     * @param start start of range
     * @param end end of range
     * @param value value to be checked if in between
     * @return Returns true if the conditions are met
     */
    public static boolean validIntRange(int start, int end, int value) {
        return (value >= start) && (value <= end);
    }

    /**
     * Finds the root of the index
     * @param array containing array
     * @param index child index
     * @return the parent of the index
     */
    public static int findRoot(int[] array,int index){

        while (index != array[index]) {
            if(array[index]==-1)return -1;
            index = array[index];
        }
        return index;
    }

    /**
     * Compiles a list of all roots in an array
     * @param array containing array
     * @return a list containing all roots in an array
     */
    public static List<Integer> getRoots(int[] array){
        List<Integer> roots = new LinkedList<>();
        for(int i=0;i< array.length;++i){
            if(i==array[i]&&!roots.contains(i)) roots.add(i);
        }
        return roots;
    }

    /**
     * Counts all groups with the same root
     * @param array containing array
     * @param index common root
     * @return amount of groups in the array
     */
    public static int groupSize(int[] array,int index){
        index = findRoot(array, index);
        int count = 0;
        for(int i = 0;i<array.length;++i){
            if(array[i]!=-1&&findRoot(array,i)==index) ++count;
        }
        return count;
    }

    /**
     * Converts an array to an image
     * @param originalImage Original image to be drawn on.
     * @param color color for components to be drawn in
     * @param pixels array containing pixel data
     * @param width width of image
     * @param height height of image
     * @return An image with all groups marked on the array
     */
    public static Image toImage(Image originalImage,Color color,int[] pixels, int width, int height){
        WritableImage image = new WritableImage(originalImage.getPixelReader(),width,height);
        for(int i=0;i<pixels.length;++i){
            if(pixels[i]!=-1&&findRoot(pixels,i)!=-1) image.getPixelWriter().setColor(i%width,i/width, color);
            //else image.getPixelWriter().setColor(i%width,i/width,Color.WHITE);
        }
        return image;
    }
    /**
     * Converts one pixel group from an array to an image.
     * @param originalImage Original image to be drawn on.
     * @param color color for components to be drawn in
     * @param pixels array containing pixel data
     * @param width width of image
     * @param height height of image
     * @param pg only this specific group will be drawn
     * @return An image with all groups marked on the array
     */
    public static Image drawGroup(Image originalImage,Color color,int[] pixels, int width, int height,PixelGroup pg){
        WritableImage image = new WritableImage(originalImage.getPixelReader(),width,height);
        for(int i=0;i<pixels.length;++i){
            if(pixels[i]!=-1&&findRoot(pixels,i)==pg.getRoot()) image.getPixelWriter().setColor(i%width,i/width, color);
            //else image.getPixelWriter().setColor(i%width,i/width,Color.WHITE);
        }
        return image;
    }

    /**
     * Marks groups around components in an image
     * @param cg the component group to be marked
     * @param image the image to be marked
     * @param lineSize the size of the lines
     * @return An image with boxes drawn around groups in the image.
     */
    public static Image markImage(ComponentGroup cg, Image image, int lineSize){
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage newImage = new WritableImage(image.getPixelReader(),width,height);
        PixelWriter pw = newImage.getPixelWriter();
        for(PixelGroup group: cg.getGroups()){
            int topRow = group.getTopRow(), bottomRow = group.getBottomRow(), leftCol = group.getLeftColumn(), rightCol = group.getRightColumn();
            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    if ((Utilities.validIntRange(topRow - lineSize, topRow + lineSize, y)
                            || Utilities.validIntRange(bottomRow - lineSize, bottomRow + lineSize, y))
                            && x >= leftCol && x <= rightCol)
                        pw.setColor(x, y, Color.RED);
                    else if ((Utilities.validIntRange(leftCol - lineSize, leftCol + lineSize, x)
                            || Utilities.validIntRange(rightCol - lineSize, rightCol + lineSize, x))
                            && y >= topRow && y <= bottomRow)
                        pw.setColor(x, y, Color.RED);
                }
            }
        }
        //updateProgress(400,400);
        return newImage;
    }

    /**
     * Marks one specific group in an image
     * @param group group to be marked
     * @param image image to be drawn upon
     * @return an image with only one group marked.
     */
    public static Image markGroup(PixelGroup group,Image image){
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        int lineSize = 1;
        WritableImage newImage = new WritableImage(image.getPixelReader(),width,height);
        PixelWriter pw = newImage.getPixelWriter();
        int topRow = group.getTopRow(), bottomRow = group.getBottomRow(), leftCol = group.getLeftColumn(), rightCol = group.getRightColumn();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if ((Utilities.validIntRange(topRow - lineSize, topRow + lineSize, y)
                        || Utilities.validIntRange(bottomRow - lineSize, bottomRow + lineSize, y))
                        && x >= leftCol && x <= rightCol)
                    pw.setColor(x, y, Color.LIGHTGREEN);
                else if ((Utilities.validIntRange(leftCol - lineSize, leftCol + lineSize, x)
                        || Utilities.validIntRange(rightCol - lineSize, rightCol + lineSize, x))
                        && y >= topRow && y <= bottomRow)
                    pw.setColor(x, y, Color.LIGHTGREEN);
            }
        }
        return newImage;
    }

    /**
     * generates a random colour
     * @return a random colour
     */
    public static Color randomColour(){
        return new Color(Math.random(),Math.random(),Math.random(),1);
    }

    /**
     * Draws a marker on the image at a coordinate
     * @param image image to be drawn
     * @param x x coordinate of point
     * @param y y coordinate of point
     * @return image with mark applied to it
     */
    public static Image markPosition(Image image,int x,int y){
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage newImage = new WritableImage(image.getPixelReader(),width,height);
        PixelWriter pw = newImage.getPixelWriter();
        int size = 4;
        int lineWidth = 1;
        int x2,y2;
        for(int i = -size;i<=size;i++) {
            for(int j = -lineWidth;j<=lineWidth;j++){
                x2 = x + i;
                y2 = y + i;
                if(j%2==0)
                    x2-=j;
                else
                    y2+=j;

                if (x2 >= 0 && y2 >= 0 && x2 < width && y2 < height) pw.setColor(x2, y2, Color.BLUE);
                x2 = x - i;
                if(j%2==0)x2+=j;
                if (x2 >= 0 && y2 >= 0 && x2 < width && y2< height) pw.setColor(x2, y2, Color.BLUE);
            }
        }
        return newImage;
    }

    //public static int[] unionMerge(int[] pixels,int index1,index2)

    /**
     * Comparison method that returns a boolean based on the similarity of two colours
     * @param color1 First colour
     * @param color2 Second colour
     * @param tolerance the amount of tolerance when comparing the two colours
     * @return A boolean based on who similar the colours are
     */
    public static boolean compareRGB(Color color1,Color color2, double tolerance){
        return color1.getRed()<=color2.getRed()+tolerance && color1.getRed()>=color2.getRed()-tolerance &&
                color1.getBlue()<=color2.getBlue()+tolerance && color1.getBlue()>=color2.getBlue()-tolerance &&
                color1.getGreen()<=color2.getGreen()+tolerance && color1.getGreen()>=color2.getGreen()-tolerance;
    }


}