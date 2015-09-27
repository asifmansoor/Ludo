package com.logicanvas.frameworks.boardgamesgdk.core.utility;

import org.junit.Test;
import static org.junit.Assert.*;
import com.logicanvas.frameworks.boardgamesgdk.core.utility.Utility;

/**
 * Created by amansoor on 23-09-2015.
 */
public class UtilityTest {
    @Test
    public void findInArrayTest() {
        int[] array = {0,1,2};
        int result = Utility.findInArray(array, 1);
        int expected = 1;
        assertEquals(expected, result);
//        System.out.println("test: "+Utility.findInArray(array, 1));
    }

}
