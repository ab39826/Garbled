package com.company.Gate;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class Wire {

    public static final int AES_KEYLENGTH=128;
    byte[][] value=new byte[2][0];

    public Wire() throws Exception
    {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(AES_KEYLENGTH);
        SecretKey skey = kgen.generateKey();
        value[0]=skey.getEncoded();
        skey = kgen.generateKey();
        value[1]=skey.getEncoded();


        //perform necessary garbling on circuit
        value[0][0]=0x12;value[0][1]=0x33;value[0][2]=0x21;
        value[1][0]=0x12;value[1][1]=0x33;value[1][2]=0x21;
    }


    //constructor for wire if final result
    public Wire(int mask)
    {
        if((mask&1)==1)
            value[0]=new byte[]{0x12,0x33,0x21,0,0,0,0,0,0,0,0,0,0,0,0,0};
        if((mask&2)==2)
            value[1]=new byte[]{0x12,0x33,0x21,1,1,1,1,1,1,1,1,1,1,1,1,1};

    }



    public byte[] getValue0()
    {
        return value[0];
    }

    public byte[] getValue1()
    {
        return value[1];
    }


}
