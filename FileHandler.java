/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

import java.io.*;
import java.util.logging.*;

public class FileHandler {

    private static final Logger logger = Logger.getLogger(FileHandler.class.getName());

    public static void readFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            logger.info("File read successfully: " + filePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading file: " + filePath, e);
        }
    }

    public static void writeFile(String filePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
            logger.info("File written successfully: " + filePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing file: " + filePath, e);
        }
    }
}
