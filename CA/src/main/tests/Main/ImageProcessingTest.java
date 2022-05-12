package Main;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class ImageProcessingTest {
    ImageProcessing ip;
    int width = 9,height = 4;
    int[] pixels = new int[width*height];
    Image image;
    @BeforeEach
    void setUp() {
        ImageApplication.main(new String[1]);
        for(int i = 0;i<pixels.length;++i){
            if(i%width==0) {pixels[i] = i;}
            else if(i%width==5||i%width==6){
                pixels[i]=i;
            }else pixels[i] = -1;
        }
        pixels[3]=3;
        pixels[4]=4;
        pixels[28] = 28;
        try {
            image = new Image(new FileInputStream("src/main/resources/images/testImage.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ip = new ImageProcessing(image,pixels,width,height,0,"Test", Color.RED,true);
    }

    @Test
    void testGrouping(){
        ip.call();
        int[] processedArray = ImageController.pixels;
        assertEquals(0,processedArray[9]);
        assertEquals(0,processedArray[18]);
        assertEquals(0,processedArray[28]);
        assertEquals(3,processedArray[4]);
        assertEquals(3,processedArray[14]);
        assertEquals(3,processedArray[23]);
        assertEquals(3,processedArray[33]);
//        for(int i = 0;i<processedArray.length;i+=width){
//            for(int j = i;j<i+width;++j) System.out.print(processedArray[j]+" ");
//            System.out.println("");
//        }
    }
}