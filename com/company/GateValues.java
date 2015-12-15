package com.company;

/**
 * Created by user on 12/4/2015.
 */
public class GateValues {

   public static final int AND = 0;
    public static final int OR = 1;
    public static final int XOR = 2;

    public static final int[][] AND_GATE = {
            { 0, 0, 0 },
            { 0, 1, 0 },
            { 1, 0, 0 },
            { 1, 1, 1 }
    };

    public static final int[][] OR_GATE = {
            { 0, 0, 0 },
            { 0, 1, 1 },
            { 1, 0, 1 },
            { 1, 1, 1 }
    };

    public static final int[][] XOR_GATE = {
            { 0, 0, 0 },
            { 0, 1, 1 },
            { 1, 0, 1 },
            { 1, 1, 0 }
    };

}
