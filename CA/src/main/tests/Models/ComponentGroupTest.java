package Models;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ComponentGroupTest {
    ComponentGroup cg = new ComponentGroup("Test", Color.RED);

    @BeforeEach
    void setUp() {
        for(int i =10;i>=0;--i){
            cg.addPixelGroup(new PixelGroup(i,i*i));
        }
    }

    @Test
    void getPixelGroupByRoot() {
        assertEquals(3,cg.getPixelGroupByRoot(9).getSize());
        assertEquals(5,cg.getPixelGroupByRoot(25).getSize());
        assertEquals(7,cg.getPixelGroupByRoot(49).getSize());
        assertEquals(10,cg.getPixelGroupByRoot(100).getSize());
    }

    @Test
    void addPixelGroup() {

        assertEquals(0,cg.getPixelGroup(0).getSize());
        assertEquals(3,cg.getPixelGroup(3).getSize());
        assertEquals(9,cg.getPixelGroup(9).getSize());
        assertEquals(10,cg.getPixelGroup(10).getSize());
    }
}