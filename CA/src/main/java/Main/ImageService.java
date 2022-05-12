package Main;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ImageService extends Service<Image> {
    int[] pixels;
    int width,height;
    double noiseFilter;
    Color color;
    Image image;
    String name;
    boolean newGroup;

    public ImageService(Image image,int[] pixels,int width,int height,double noiseFilter,String name,Color color,boolean newGroup) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        this.image = image;
        this.noiseFilter = noiseFilter;
        this.color = color;
        this.name = name;
        this.newGroup = newGroup;
    }

    @Override
    protected Task<Image> createTask() {
        return new ImageProcessing(image, pixels,width,height,noiseFilter,name,color,newGroup);
    }
}
