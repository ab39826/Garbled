package com.company;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Created by user on 12/6/2015.
 */
public class EvaluateNode extends YaoNode {
    GarbledGate gate;
    OTuple<SecretKey> inputTuple;
    SecretKey outputKey;
    EvaluateNode leftChild;
    EvaluateNode rightChild;



    public EvaluateNode(){
        gate = new GarbledGate();
        inputTuple = null;
        leftChild = null;
        rightChild  = null;
        outputKey = null;
    }


    public void evaluateNode(){

        //need to just evaluate and set outputKey. can be found with garbled gate, and input tuple

        //first get input tuple from children


            if(inputTuple == null){
                inputTuple = new OTuple<SecretKey>(leftChild.outputKey,rightChild.outputKey);
            }



        //check all possible decryption combinations for match for correct output key
        for(int index = 0; index < gate.garbledList.length; index++){
            if(degarble(index)){
                break;
            }
        }



    }


    public boolean degarble(int index){

    //degarble and check to see whether secret key matches

        GarbledValue gGate = gate.garbledList[index];
        SecretKey ikey = inputTuple.zero;
        SecretKey jkey = inputTuple.one;
        SecretKey potentialOutputKey = null;



        try{
            //decoding/unpacking potential output key
            byte[] outerDecrypted = symCipher.decrypt(gGate.data,ikey);
            byte[] innerDecrypted = symCipher.decrypt(outerDecrypted,jkey);
            String keyString = new String(innerDecrypted, "UTF8");
            byte[] decodedKeyBytes = Base64.getDecoder().decode(keyString);
             potentialOutputKey = new SecretKeySpec(decodedKeyBytes,0,decodedKeyBytes.length, "AES");

        }
        catch(Exception e){
            return false;
        }


        //check for output key match
        if((potentialOutputKey.hashCode() == gate.hashTuple.zero) || (potentialOutputKey.hashCode() == gate.hashTuple.one)){

            outputKey = potentialOutputKey;
            return true;
        }

        return false;
    }


}
