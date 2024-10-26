/** 
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */

import java.security.*;
import java.util.Base64;

public class SimpleBitcoinWallet {

    private KeyPair keyPair;
    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    public SimpleBitcoinWallet() throws NoSuchAlgorithmException {
        generateKeyPair();
    }

    private void generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        keyPair = keyPairGenerator.generateKeyPair();
    }

    public String getPublicKey() {
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    public String getPrivateKey() {
        return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    public String signMessage(String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(keyPair.getPrivate());
        signature.update(message.getBytes());
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    public boolean verifyMessage(String message, String signatureStr) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(keyPair.getPublic());
        signature.update(message.getBytes());
        byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);
        return signature.verify(signatureBytes);
    }

    public static void main(String[] args) {
        try {
            SimpleBitcoinWallet wallet = new SimpleBitcoinWallet();

            // Display public and private keys
            System.out.println("Public Key: " + wallet.getPublicKey());
            System.out.println("Private Key: " + wallet.getPrivateKey());

            // Sign and verify a message
            String message = "Sample transaction message";
            String signature = wallet.signMessage(message);
            System.out.println("Signature: " + signature);
            boolean isValid = wallet.verifyMessage(message, signature);
            System.out.println("Is the signature valid? " + isValid);

        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
    }
}

/** 
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */
