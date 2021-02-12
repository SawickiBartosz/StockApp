package pl.edu.pw.mini.zpoif.projekt.jung_sawicki;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class MovingAvgTest {

    @Test
    void createAvgList() {
        Float[] numbers = {1f,1f, 1f,1f,1f,1f,1f,1f,1f,1f,1f,1f};
        ArrayList<Number> arraylist = new ArrayList<>(Arrays.asList(numbers));
        ArrayList<Number> result = MovingAvg.createAvgList(arraylist,1);
        assertEquals(1.0, result.get(3),"Czy wszystkie równe 1");
    }

    @Test
    void createAvgList2() {
        Float[] numbers = {1f,2f, 1f,2f,1f,2f,1f,2f,1f,2f,1f,2f};
        ArrayList<Number> arraylist = new ArrayList<>(Arrays.asList(numbers));
        ArrayList<Number> result = MovingAvg.createAvgList(arraylist,1);
        assertEquals(1.0, result.get(4),"Czy równy 1");
        result = MovingAvg.createAvgList(arraylist,2);
        assertEquals( 1.5, result.get(1));
        assertEquals(11, result.size());
        result = MovingAvg.createAvgList(arraylist,3);
        assertEquals(10, result.size());

    }
}