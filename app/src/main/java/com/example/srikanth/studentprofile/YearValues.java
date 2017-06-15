package com.example.srikanth.studentprofile;

/**
 * Created by Srikanth on 6/9/2017.
 */

public class YearValues {

    public static Integer[] fromYearValues = new Integer[50];
    public static Integer[] toYearValues = new Integer[50];

    public static void yearCreation() {
        for (int i = 0; i < 50; i++) {
            fromYearValues[i]=(2017-i);
            toYearValues[i]=(2017-i);
        }
    }
}
