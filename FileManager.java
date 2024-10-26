import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 *
 * FileManager handles file operations such as copying a file to a USB drive.
 */
public class FileManager {

    private static final Logger logger = Logger.getLogger(FileManager.class.getName());
    private static String USB_DRIVE_PATH;

    static {
        // Configure logging
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.ALL);

        // Load configuration
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
            USB_DRIVE_PATH = properties.getProperty("usbDrivePath", "/media/usb");
        } catch (IOException e) {
            logger.warning("Failed to load configuration. Using default USB drive path.");
            USB_DRIVE_PATH = "/media/usb";
        }
    }

    /**
     * Copies a file from the source path to a specified name on the USB drive.
     * 
     * @param sourceFilePath The path of the source file to be copied.
     * @param targetFileName The name of the file on the USB drive.
     */
    public static void placeOnUsbDrive(String sourceFilePath, String targetFileName) {
        File usbDrive = new File(USB_DRIVE_PATH);

        // Check if the USB drive is available
        if (!usbDrive.exists() || !usbDrive.isDirectory()) {
            logger.severe("USB drive not found or is not a directory.");
            return;
        }

        File sourceFile = new File(sourceFilePath);
        File targetFile = new File(usbDrive, targetFileName);

        // Check if the source file exists
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            logger.severe("Source file does not exist or is not a file.");
            return;
        }

        // Check file size (limit set to 100MB for example)
        final long MAX_FILE_SIZE = 100 * 1024 * 1024;
        if (sourceFile.length() > MAX_FILE_SIZE) {
            logger.severe("Source file is too large. Maximum allowed size is " + (MAX_FILE_SIZE / (1024 * 1024)) + "MB.");
            return;
        }

        // Ensure source file has read permission
        if (!sourceFile.canRead()) {
            logger.severe("Source file does not have read permission.");
            return;
        }

        // Ensure target directory exists
        File targetDir = targetFile.getParentFile();
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            logger.severe("Failed to create target directory: " + targetDir.getAbsolutePath());
            return;
        }

        // Ensure the target directory is writable
        if (!targetDir.canWrite()) {
            logger.severe("Target directory is not writable.");
            return;
        }

        // Copy the file with progress monitoring and retry mechanism
        int retryCount = 3;
        while (retryCount > 0) {
            try (InputStream in = new FileInputStream(sourceFile);
                 OutputStream out = new FileOutputStream(targetFile)) {

                long totalBytes = sourceFile.length();
                long bytesCopied = 0;
                byte[] buffer = new byte[8192]; // Optimized buffer size for better performance
                int bytesRead;

                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                    bytesCopied += bytesRead;

                    // Log progress
                    double progress = ((double) bytesCopied / totalBytes) * 100;
                    logger.info(String.format("Copying progress: %.2f%%", progress));
                }

                // Verify file integrity
                if (verifyFileIntegrity(sourceFile, targetFile)) {
                    logger.info("File successfully copied and verified on USB drive: " + targetFile.getAbsolutePath());
                    return; // Exit if successful
                } else {
                    logger.severe("File integrity check failed after copying.");
                }

            } catch (IOException e) {
                logger.warning("Error copying file to USB drive: " + e.getMessage() + ". Retrying...");
                retryCount--;
                if (retryCount == 0) {
                    logger.severe("Failed to copy file after several attempts.");
                }
                try {
                    Thread.sleep(2000); // Wait before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // Restore interrupt status
                }
            }
        }
    }

    /**
     * Verifies the integrity of the copied file by comparing checksums.
     * 
     * @param sourceFile The original source file.
     * @param targetFile The copied file on the USB drive.
     * @return true if the file is verified, false otherwise.
     */
    private static boolean verifyFileIntegrity(File sourceFile, File targetFile) {
        try {
            byte[] sourceChecksum = calculateChecksum(sourceFile);
            byte[] targetChecksum = calculateChecksum(targetFile);
            return MessageDigest.isEqual(sourceChecksum, targetChecksum);
        } catch (IOException | NoSuchAlgorithmException e) {
            logger.severe("Error verifying file integrity: " + e.getMessage());
            return false;
        }
    }

    /**
     * Calculates the checksum of a file using SHA-256.
     * 
     * @param file The file for which the checksum is to be calculated.
     * @return The checksum as a byte array.
     * @throws IOException If an I/O error occurs.
     * @throws NoSuchAlgorithmException If the algorithm is not available.
     */
    private static byte[] calculateChecksum(File file) throws IOException, NoSuchAlgorithmException {
        try (InputStream fis = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192]; // Optimized buffer size
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            return digest.digest();
        }
    }

    public static void main(String[] args) {
        // Example usage
        if (args.length < 2) {
            System.out.println("Usage: java FileManager <sourceFilePath> <targetFileName>");
            return;
        }
        String sourceFilePath = args[0];
        String targetFileName = args[1];
        placeOnUsbDrive(sourceFilePath, targetFileName);
    }
}

/*
 * This code defines a FileManager class that handles file operations such as copying a file to a USB drive. Here's a breakdown of what it does:
 *
 * Logging Configuration:
 * - Configures logging using ConsoleHandler and sets the logging level to ALL.
 *
 * Load Configuration:
 * - Loads the USB drive path from a config.properties file. If the file is not found, it uses a default path.
 *
 * Copy File to USB Drive:
 * - Checks if the USB drive is available and writable.
 * - Checks if the source file exists, is readable, and is within the size limit (100MB).
 * - Copies the file to the USB drive with progress monitoring and a retry mechanism.
 * - Verifies the integrity of the copied file by comparing checksums.
 *
 * Checksum Calculation:
 * - Calculates the checksum of a file using SHA-256 to verify file integrity.
 *
 * Main Method:
 * - Provides an example usage of the placeOnUsbDrive method.
 */
