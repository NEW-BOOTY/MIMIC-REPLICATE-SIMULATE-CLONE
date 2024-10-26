/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Storage.BlobTargetOption;
import com.google.cloud.storage.StorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GCSUploadWithEncryption {

    public static void main(String[] args) {
        String projectId = "your-project-id";
        String bucketName = "your-bucket-name";
        String objectName = "your-object-name";
        String filePath = "path-to-your-file";
        
        // Optional: Customer-supplied encryption key (base64 encoded)
        String encryptionKey = "your-base64-encoded-encryption-key";

        try {
            uploadFileWithEncryption(projectId, bucketName, objectName, filePath, encryptionKey);
        } catch (IOException | StorageException e) {
            System.err.println("Error uploading file: " + e.getMessage());
        }
    }

    public static void uploadFileWithEncryption(String projectId, String bucketName, String objectName, String filePath, String encryptionKey) throws IOException {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

        Path path = Paths.get(filePath);
        byte[] data = Files.readAllBytes(path);
        
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        
        // Upload the file with server-side encryption (Google-managed or customer-supplied key)
        Storage.BlobTargetOption option = encryptionKey != null
                ? BlobTargetOption.encryptionKey(encryptionKey)
                : BlobTargetOption.kmsKeyName("your-kms-key-name"); // If using KMS key

        storage.create(blobInfo, data, option);
        System.out.println("File uploaded with encryption to bucket " + bucketName);
    }
}
