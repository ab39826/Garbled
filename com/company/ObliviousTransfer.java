package com.company;


import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Random;

public class ObliviousTransfer {

public ObliviousTransfer(){

}

    public void transfer(Garbler garbler, Evaluator evaluator){

        SecretKey[] evalKeys = garbler.obliviousTransfer(evaluator.inputBits, false);
        SecretKey[] garbKeys = garbler.obliviousTransfer(garbler.inputBits,true);

        evaluator.evaluatorBits = evalKeys;
        evaluator.garblerBits = garbKeys;


    }

    public void transferDH(Garbler garbler, Evaluator evaluator){
        Random rand=new Random();

        for(int i = 0; i< evaluator.evaluatorBits.length; i++){

            String xform = "RSA/ECB/PKCS1Padding";
            // Generate a key-pair

            try{
                KeyFactory fact = KeyFactory.getInstance("RSA");

                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(128);
                KeyPair keypair = kpg.genKeyPair();
                PrivateKey 			garbler_pk=keypair.getPrivate();
                RSAPrivateKeySpec garbler_priv=fact.getKeySpec(garbler_pk,RSAPrivateKeySpec.class);
                BigInteger garbler_N= garbler_priv.getModulus();
                BigInteger 			garbler_c=garbler_priv.getPrivateExponent();
                BigInteger 			garbler_m0=new BigInteger(Integer.toString(rand.nextInt(1000000)),10);
                BigInteger 			garbler_m1=new BigInteger(Integer.toString(rand.nextInt(1000000)),10);
                BigInteger 			garbler_x0=new BigInteger(1024,rand);
                BigInteger		 	garbler_x1=new BigInteger(1024,rand);

                PublicKey 			evaluator_publickey=keypair.getPublic();
                RSAPublicKeySpec evaluator_pub = fact.getKeySpec(evaluator_publickey,RSAPublicKeySpec.class);
                BigInteger 			evaluator_N=evaluator_pub.getModulus();
                BigInteger 			evaluator_e=evaluator_pub.getPublicExponent();

                BigInteger 			evaluator_x0=garbler_x0;
                BigInteger 			evaluator_x1=garbler_x1;

                BigInteger 			evaluator_secretx= evaluator_x1; //wir wollen m1 haben
                BigInteger			evaluator_k=new BigInteger(1024,rand);
                BigInteger 			evaluator_v= evaluator_secretx.add(evaluator_k.pow(evaluator_e.intValue())).mod(evaluator_N);
                BigInteger 			garbler_v=evaluator_v;
                evaluator.inputBits[i] = garbler_v.intValue();
            }
            catch(Exception e){
                System.out.println("OT exception occurred");
            }

        }




    }


}
