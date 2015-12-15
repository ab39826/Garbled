

        package com.company;

        import java.security.Key;
        import java.security.NoSuchAlgorithmException;
        import java.security.SecureRandom;
        import java.security.spec.KeySpec;
        import java.util.Base64;
        import javax.crypto.Cipher;
        import javax.crypto.KeyGenerator;
        import javax.crypto.SecretKey;
        import javax.crypto.spec.IvParameterSpec;
        import javax.crypto.spec.SecretKeySpec;
        import javax.swing.plaf.synth.SynthMenuBarUI;


        public class SymmetricCipher {
    static IvParameterSpec iv;

    public SymmetricCipher () {
        SecureRandom random = new SecureRandom();
        iv = new IvParameterSpec(random.generateSeed(16));

    }

    public final static SecretKey generateKey() throws NoSuchAlgorithmException{
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        SecureRandom random = new SecureRandom();
        kg.init(random);
        return kg.generateKey();
    }

    public static byte[] encrypt(byte[] inpBytes, final Key key) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,key, SymmetricCipher.iv);

        return cipher.doFinal(inpBytes);
    }

    public static final byte[] decrypt(byte[] decBytes,final Key key) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, SymmetricCipher.iv);

        byte[] stringBytes = cipher.doFinal(decBytes);
        return stringBytes;
    }


    public static void testAES() throws Exception{
/*
        Key k = SymmetricCipher.generateKey();
        Key kprime = SymmetricCipher.generateKey();



        String clearText = "ayo whtsurnameGirl";
        System.out.println("Clear Text:" + clearText);

        byte[] messageBytes = clearText.getBytes();
        byte[] encryptedBytes = SymmetricCipher.encrypt(messageBytes,k);
        System.out.println("Encrypted String:" + encryptedBytes.toString());

        byte[] decryptedBytes = SymmetricCipher.decrypt(encryptedBytes,k);
        String finalText = new String(decryptedBytes, "UTF8");

        System.out.println("Decrypted String: " + finalText);


        GarbledValue transform = new GarbledValue(decryptedBytes);

        OTuple<Key> tup = new OTuple<Key>(k,kprime);



        String tripleCheck = new String(transform.data, "UTF8");

        System.out.println("Check Check "+ tripleCheck);
*/

        /////////////////////

        /*
        Key outie = SymmetricCipher.generateKey();
        Key innie = SymmetricCipher.generateKey();

        String payload = "butts everywhere";
        System.out.println("Original string is "+payload);
        byte[] innerEncrypted = SymmetricCipher.encrypt(payload.getBytes(),innie);

        String innerString = new String(innerEncrypted, "UTF8");
        System.out.println("Inner Encrypted string is " + innerString);

        //now encrypt this with outie

        byte[] outerEncrypted = SymmetricCipher.encrypt(innerEncrypted,outie);
        String outerString = new String(outerEncrypted, "UTF8");
        System.out.println("Outer Encrypted string is " + outerString);

        //////////////////////

        //now undo operations and validate

        byte[] outerDecrypted = SymmetricCipher.decrypt(outerEncrypted,outie);
        String outerDecryptString = new String(outerDecrypted, "UTF8");
        System.out.println("Outer Decrypted string is " + outerDecryptString);

        byte[] innerDecrypted = SymmetricCipher.decrypt(innerEncrypted,innie);
        String fullyDecrypted = new String(innerDecrypted, "UTF8");
        System.out.println("back that ass up" + fullyDecrypted);
        */

        ////////////////////


        long lStartTime = System.currentTimeMillis();
        SecretKey innie = SymmetricCipher.generateKey();

        String payload = "butts everywhere";
        System.out.println("Original string is "+payload);
        byte[] innerEncrypted = SymmetricCipher.encrypt(payload.getBytes(),innie);

        String encodedKey = Base64.getEncoder().encodeToString(innie.getEncoded());

        Key outie = SymmetricCipher.generateKey();
        byte[] outerEncrypted = SymmetricCipher.encrypt(encodedKey.getBytes(),outie);
        byte[] outerDecrypted = SymmetricCipher.decrypt(outerEncrypted,outie);

        String stringCheese = new String(outerDecrypted, "UTF8");

        byte[] decodedKey = Base64.getDecoder().decode(stringCheese);

        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        byte[] innerDecrypted = SymmetricCipher.decrypt(innerEncrypted,originalKey);

        String wowee = new String(innerDecrypted, "UTF8");

        System.out.println("Big money big money no whamme: "+wowee);

        long lEndTime = System.currentTimeMillis();

        long difference = lEndTime - lStartTime;

        System.out.println("Elapsed milliseconds: " + difference);

    }

}

