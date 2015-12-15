package com.company;

// File: src\jsbook\ch3\AsymmetricCipherTest.java
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import javax.crypto.Cipher;

public class AsymmetricCipher {
    private static byte[] encrypt(byte[] inpBytes, PublicKey key,
                                  String xform) throws Exception {
        Cipher cipher = Cipher.getInstance(xform);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(inpBytes);
    }
    private static byte[] decrypt(byte[] inpBytes, PrivateKey key,
                                  String xform) throws Exception{
        Cipher cipher = Cipher.getInstance(xform);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(inpBytes);
    }

    public static void testRSA() throws Exception {


        String xform = "RSA/ECB/PKCS1Padding";
        // Generate a key-pair
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024); // 1024 is the keysize.
        KeyPair kp = kpg.generateKeyPair();
        PublicKey pubk = kp.getPublic();
        PrivateKey prvk = kp.getPrivate();


        long lStartTime = System.currentTimeMillis();
        byte[] dataBytes =
                "yeppers".getBytes();

        byte[] encBytes = encrypt(dataBytes, pubk, xform);
        long lEndTime = System.currentTimeMillis();

        long difference = lEndTime - lStartTime;

        System.out.println("Elapsed milliseconds for encryption only: " + difference);
        byte[] decBytes = decrypt(encBytes, prvk, xform);
        boolean expected = java.util.Arrays.equals(dataBytes, decBytes);
        System.out.println("Test " + (expected ? "SUCCEEDED!" : "FAILED!"));
    }
}