package Utilities;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilitiesTest{

    @Test
    void validIndex() {
        List<String> list = new ArrayList<>();
        for(int i = 0;i<20;++i) list.add("String "+i);
        assertTrue(Utilities.validIndex(0,list));
        assertTrue(Utilities.validIndex(12,list));
        assertFalse(Utilities.validIndex(20,list));
        assertFalse(Utilities.validIndex(-2,list));
    }

    @Test
    void validIntRange() {
        assertTrue(Utilities.validIntRange(1,10,5));
        assertTrue(Utilities.validIntRange(1,3,3));
        assertFalse(Utilities.validIntRange(1,5,0));
        assertFalse(Utilities.validIntRange(1,5,6));
    }

    @Test
    void randomColour() {
        assertNotNull(Utilities.randomColour());
    }

    @Test
    void compareRGB() {
        Color color1 = new Color(.5,.5,.5,1), color2 = new Color(.6,.6,.6,1);
        assertTrue(Utilities.compareRGB(color1,color2,.12));
        assertFalse(Utilities.compareRGB(color1,color2,.05));
    }
}