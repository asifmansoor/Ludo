package com.logicanvas.frameworks.boardgamesgdk.core.utility;

/**
 * Created by amansoor on 23-09-2015.
 */
public class Utility {
    public static int findInArray(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (value == array[i]) {
                return i;
            }
        }

        return -1;
    }
}
