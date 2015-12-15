package com.company;



//basic security crpytographic imports
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Random;


import com.company.Gate.Gate;

    public class SecurityOps
    {

        //swaps 2 byte arrays
        public static void swap(byte[] a,byte[] b)
        {
            byte[] temp;
            temp=a;
            a=b;
            b=temp;
        }


        public static void shuffleLUT(byte[][] lut)
        {
            // If running on Java 6 or older, use `new Random()` on RHS here
            Random rand = new Random();

            for (int i = lut.length- 1; i > 0; i--)
            {
                int index = rand.nextInt(i + 1);
                swap(lut[i], lut[index]);
            }
        }


        //basic utility functions for debugging
        public static void printLUT(Gate gate, String title)
        {
            System.out.println(title);

            for(int i=0;i<4;i++)
            {
                System.out.println(getHex(gate.getLutEntry(i)));
            }
            System.out.println();
        }

        public static String getHex(byte[] byteArray)
        {
            String result="";
            for(int j=0;j<byteArray.length;j++)
            {
                String hex="0"+Integer.toHexString(byteArray[j]&255);
                result+=hex.substring(hex.length()-2);
            }
            return result;
        }

        public static boolean arraysAreEqual(byte[] a,byte[] b)
        {
            if(a.length!=b.length)
                return false;

            for(int i=0;i<a.length;i++)
                if(a[i]!=b[i])
                    return false;

            return true;
        }


        public static byte[] genAESkey(int size) throws Exception
        {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(size);

            SecretKey skey = kgen.generateKey();
            byte[] raw = skey.getEncoded();

            return raw;
        }
        public static byte[] AESdecrypt(byte[] encrypted,byte[] key) throws Exception
        {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] plain=cipher.doFinal(encrypted);
            return plain;
        }

        public static byte[] AESencrypt(byte[] plain,byte[] key) throws Exception
        {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted =cipher.doFinal(plain);
            return encrypted;
        }

        public static KeyPair genRSAkeypair() throws Exception
        {
            System.out.println("gen0");

            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            System.out.println("gen1");
            kpg.initialize(1024);
            System.out.println("gen2");
            KeyPair kp = kpg.genKeyPair();
            System.out.println("gen3");
            return kp;


        }

        public static byte[] RSAencrypt(byte[] data,PublicKey key) throws Exception {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherData = cipher.doFinal(data);
            return cipherData;
        }

        public static byte[] RSAdecrypt(byte[] data,PrivateKey key) throws Exception
        {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] cipherData = cipher.doFinal(data);
            return cipherData;
        }

    }


