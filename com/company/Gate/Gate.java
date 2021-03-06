package com.company.Gate;

import com.company.SecurityOps;

/**
 * Created by user on 10/11/2015.
 */
public class Gate {

    //Assumption. Every gate can be constructed through the use of a few key logical gates.

    //gate class defines a couple of things.

    //1. Look up table for garbled truth table elements

    byte[][] lut=new byte[4][];

    public Gate(){}

    public Gate(byte[] l1,byte[] l2,byte[] l3,byte[] l4)
    {
        lut[0]=l1;
        lut[1]=l2;
        lut[2]=l3;
        lut[3]=l4;
    }

    public Gate(byte[][] lut)
    {
        this.lut=lut;
    }

    public byte[] operate(byte[] key1, byte[] key2) throws Exception
    {
        byte[] result1=null;
        byte[] result2=null;

        for(int i=0;i<4;i++)
        {
            result1= SecurityOps.AESdecrypt(lut[i],key1);
            result2= SecurityOps.AESdecrypt(result1,key2);

            if(result2[0]==0x12&&result2[1]==0x33&&result2[2]==0x21)
                return result2;
        }

        return null;
    }

    void genEncryptedLut(int i00,int i01,int i10,int i11,Wire i1, Wire i2, Wire r) throws Exception
    {
        //encrypt
        lut[0]= SecurityOps.AESencrypt(SecurityOps.AESencrypt(r.value[i00],i1.value[0]),i2.value[0]);
        lut[1]= SecurityOps.AESencrypt(SecurityOps.AESencrypt(r.value[i01],i1.value[1]),i2.value[0]);
        lut[2]= SecurityOps.AESencrypt(SecurityOps.AESencrypt(r.value[i10],i1.value[0]),i2.value[1]);
        lut[3]= SecurityOps.AESencrypt(SecurityOps.AESencrypt(r.value[i11],i1.value[1]),i2.value[1]);


        //shuffle to improve security
        SecurityOps.shuffleLUT(lut);
    }

    public byte[] getLutEntry(int i)
    {
        return lut[i];
    }

    public byte[][] getLut()
    {
        return lut;
    }

}
