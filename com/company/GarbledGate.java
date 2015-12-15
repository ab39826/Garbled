package com.company;

import com.company.GarbledValue;
import com.company.OTuple;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by user on 12/5/2015.
 */
public class GarbledGate {
   GarbledValue[] garbledList;
    OTuple<Integer> hashTuple;

    public GarbledGate(){
        garbledList = new GarbledValue[4];
        hashTuple = new OTuple<Integer>();
    }

}
