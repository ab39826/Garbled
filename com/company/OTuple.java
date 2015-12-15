package com.company;

import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;

/**
 * Created by user on 12/4/2015.
 */
public class OTuple<T> {

    T zero;
    T one;

    public OTuple(){
        zero = null;
        one = null;
    }

    public OTuple(T zeroVal, T oneVal){
        zero = zeroVal;
        one = oneVal;
    }


    public T getZero(){
        return zero;
    }

    public T getOne(){
        return one;
    }

}
