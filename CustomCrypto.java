/** 
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */// CustomCrypto.javaimport javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.Base64;
import java.security.spec.InvalidKeySpecException;

publicclassCustomCrypto {

    privatestaticfinalStringAES_ALGORITHM="AES/GCM/NoPadding";
    privatestaticfinalintAES_KEY_SIZE=256;
    privatestaticfinalintIV_SIZE=12; // AES/GCM standard IV sizeprivatestaticfinalintTAG_SIZE=128; // AES/GCM standard tag sizepublicstaticvoidmain(String[] args) {
        try {
            // Generate AES keySecretKeykey= generateAESKey();

            // Define plaintextStringplaintext="Sensitive Information";

            // Encrypt the plaintextbyte[] encryptedText = encryptAES(key, plaintext);
            System.out.println("Encrypted text (Base64): " + Base64.getEncoder().encodeToString(encryptedText));

            // Decrypt the ciphertextStringdecryptedText= decryptAES(key, encryptedText);
            System.out.println("Decrypted text: " + decryptedText);

        } catch (GeneralSecurityException e) {
            System.err.println("Security exception occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    privatestatic SecretKey generateAESKey()throws NoSuchAlgorithmException {
        try {
            KeyGeneratorkeyGen= KeyGenerator.getInstance("AES");
            keyGen.init(AES_KEY_SIZE);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            thrownewNoSuchAlgorithmException("AES algorithm not available.", e);
        }
    }

    privatestaticbyte[] encryptAES(SecretKey key, String plaintext) throws GeneralSecurityException {
        Ciphercipher= Cipher.getInstance(AES_ALGORITHM);
        byte[] iv = newbyte[IV_SIZE];
        SecureRandomrandom=newSecureRandom();
        random.nextBytes(iv);
        
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, newGCMParameterSpec(TAG_SIZE, iv));
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes());
            // Combine IV and ciphertextbyte[] encryptedText = newbyte[IV_SIZE + ciphertext.length];
            System.arraycopy(iv, 0, encryptedText, 0, IV_SIZE);
            System.arraycopy(ciphertext, 0, encryptedText, IV_SIZE, ciphertext.length);
            return encryptedText;
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            thrownewGeneralSecurityException("Encryption failed.", e);
        }
    }

    privatestatic String decryptAES(SecretKey key, byte[] encryptedText)throws GeneralSecurityException {
        Ciphercipher= Cipher.getInstance(AES_ALGORITHM);
        byte[] iv = newbyte[IV_SIZE];
        byte[] ciphertext = newbyte[encryptedText.length - IV_SIZE];
        
        // Extract IV and ciphertext
        System.arraycopy(encryptedText, 0, iv, 0, IV_SIZE);
        System.arraycopy(encryptedText, IV_SIZE, ciphertext, 0, ciphertext.length);
        
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, newGCMParameterSpec(TAG_SIZE, iv));
            byte[] plaintextBytes = cipher.doFinal(ciphertext);
            returnnewString(plaintextBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            thrownewGeneralSecurityException("Decryption failed.", e);
        }
    }
}

/** 
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */