package com.company;

import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import javax.crypto.SecretKey;

/**
 * Created by user on 12/4/2015.
 */
public class GateNode extends YaoNode {
    int[][] basicTT;
    Key[][] keyTT;
    String type;


    OTuple<SecretKey> keyTuple;


    public GateNode leftChild;
    public GateNode rightChild;




    public GateNode(){
        try{
            keyTuple = new OTuple<SecretKey>(symCipher.generateKey(),symCipher.generateKey());
        }
        catch (Exception e){
            System.out.println("Input bit key generation failing");
        }

    }

    public GateNode (int[][] basictTruthTable, String ty){
        basicTT = basictTruthTable;
        keyTT = new Key[4][3];
        type = ty;


        leftChild = null;
        rightChild = null;
    }


    //populate keyTruthTable by using current output tuple keys for generating 3rd column values and use left/right childs for the 1st 2 columns. Place  key0 or key1 based on basicTruthTable
    public void generateOutputKeys(){
        OTuple<SecretKey> leftTuple = leftChild.keyTuple;
        OTuple<SecretKey> rightTuple = rightChild.keyTuple;



        //first generate output keys randomly for current gate node
        try{
            keyTuple = new OTuple<SecretKey>(symCipher.generateKey(),symCipher.generateKey());
        }
        catch(Exception e){
            System.out.println("Key tuple generation messed up");
        }

        generateOutputHash();




        //have three separate for loops to update columns


        //i inputs
        for(int index = 0; index < basicTT.length; index++){
            if(basicTT[index][0] == 0){
                keyTT[index][0] = leftTuple.zero;
            }
            else{
                keyTT[index][0] = leftTuple.one;
            }
        }

        //j inputs
        for(int index = 0; index < basicTT.length; index++){
            if(basicTT[index][1] == 0){
                keyTT[index][1] = rightTuple.zero;
            }
            else{
                keyTT[index][1] = rightTuple.one;
            }
        }

        //k outputs
        for(int index = 0; index < basicTT.length; index++){
            if(basicTT[index][2] == 0){
                keyTT[index][2] = keyTuple.zero;
            }
            else{
                keyTT[index][2] = keyTuple.one;
            }
        }

    }

    public void initializeInputKeys(){

        leftChild = new GateNode();
        rightChild = new GateNode();

    }

    // populate garbledList with output keys from 3rd column keyTruthTable that have been double encrypted by every combo of input keys
    public GarbledValue[] generateGarbled(){

        GarbledValue[] garbledList = new GarbledValue[4];

        for(int index = 0; index < garbledList.length; index++){


            //first encrypt output key with input j.

            //then encrypt that result with input i
            try{
                byte[] innerEncryption = symCipher.encrypt(Base64.getEncoder().encodeToString(keyTT[index][2].getEncoded()).getBytes(),keyTT[index][1]);
                byte[] outerEncryption = symCipher.encrypt(innerEncryption, keyTT[index][0]);
                garbledList[index] = new GarbledValue(outerEncryption);

            }
            catch (Exception e){
                System.out.println("Encryption first or second failed ");
            }


        }


        return garbledList;
    }


    public OTuple<Integer> generateOutputHash() {

        OTuple<Integer> hashTuple = null;
        try{
            hashTuple = new OTuple<Integer>(keyTuple.zero.hashCode(),keyTuple.one.hashCode());
        }
        catch(Exception e){
            System.out.println("Hash tuple generation messed up");
        }

        return hashTuple;
    }

}
