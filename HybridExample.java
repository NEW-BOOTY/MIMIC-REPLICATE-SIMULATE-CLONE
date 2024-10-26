/** 
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */
// This sophisticated command-line utility for hybrid encryption leverages Tink's integration with cloud KMS for key management and supports additional encryption templates.
// It requires specific arguments for operation mode, key-file, input-file, output-file, and optional context-info.

package hybrid;

import com.google.crypto.tink.*;
import com.google.crypto.tink.config.TinkConfig;
import com.google.crypto.tink.hybrid.HybridKeyTemplates;
import com.google.crypto.tink.integration.awskms.AwsKmsClient;
import com.google.crypto.tink.integration.gcpkms.GcpKmsClient;
import com.google.crypto.tink.integration.hcvault.HcVaultKmsClient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Optional;

/**
 * A sophisticated command-line utility for hybrid encryption.
 *
 * <p>It leverages Tink's integration with cloud KMS for key management and supports additional encryption templates.
 *
 * <p>It requires the following arguments:
 *
 * <ul>
 *   <li>mode: either 'encrypt' or 'decrypt'.
 *   <li>key-file: Read the key material from this file or KMS URI.
 *   <li>input-file: Read the input from this file.
 *   <li>output-file: Write the result to this file.
 *   <li>[optional] context-info: Bind the encryption to this context info.
 * </ul>
 */
public final class HybridExample {
    public static void main(String[] args) throws Exception {
        if (args.length != 4 && args.length != 5) {
            System.err.printf("Expected 4 or 5 parameters, got %d\n", args.length);
            System.err.println(
                    "Usage: java HybridExample encrypt/decrypt key-file input-file output-file [context-info]");
            System.exit(1);
        }

        String mode = args[0];
        if (!mode.equals("encrypt") && !mode.equals("decrypt")) {
            System.err.println("Incorrect mode. Please select 'encrypt' or 'decrypt'.");
            System.exit(1);
        }

        Path keyFile = Paths.get(args[1]);
        Path inputFile = Paths.get(args[2]);
        byte[] input = Files.readAllBytes(inputFile);
        Path outputFile = Paths.get(args[3]);
        byte[] contextInfo = args.length == 5 ? args[4].getBytes() : new byte[0];

        // Initialize Tink with all available key types.
        TinkConfig.register();

        // Read the keyset into a KeysetHandle.
        KeysetHandle handle = readKeysetHandle(keyFile);

        if ("encrypt".equals(mode)) {
            // Get the primitive.
            HybridEncrypt encryptor = handle.getPrimitive(HybridEncrypt.class);

            // Use the primitive to encrypt data.
            byte[] ciphertext = encryptor.encrypt(input, contextInfo);
            Files.write(outputFile, ciphertext);
        } else {
            HybridDecrypt decryptor = handle.getPrimitive(HybridDecrypt.class);

            // Use the primitive to decrypt data.
            byte[] plaintext = decryptor.decrypt(input, contextInfo);
            Files.write(outputFile, plaintext);
        }
    }

    private static KeysetHandle readKeysetHandle(Path keyFile) throws GeneralSecurityException, IOException {
        String keyUri = new String(Files.readAllBytes(keyFile));
        Optional<KmsClient> kmsClient = getKmsClient(keyUri);

        return kmsClient.map(client -> client.getAead(keyUri))
                .map(Aead::getKeysetHandle)
                .orElseGet(() -> TinkJsonProtoKeysetFormat.parseKeyset(keyUri));
    }

    private static Optional<KmsClient> getKmsClient(String keyUri) {
        if (keyUri.startsWith("aws-kms://")) {
            return Optional.of(new AwsKmsClient().withDefaultCredentials());
        } else if (keyUri.startsWith("gcp-kms://")) {
            return Optional.of(new GcpKmsClient().withDefaultCredentials());
        } else if (keyUri.startsWith("vault://")) {
            return Optional.of(new HcVaultKmsClient().withDefaultCredentials());
        }
        return Optional.empty();
    }

    private HybridExample() {}
}
