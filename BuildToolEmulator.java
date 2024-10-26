/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildToolEmulator {

    public static void main(String[] args) {
        String buildTool = determineBuildTool();
        if (buildTool.isEmpty()) {
            return; // Exit if no build tool is found
        }

        String buildConfig = readBuildConfig(buildTool);
        if (buildConfig.isEmpty()) {
            return; // Exit if there's an error reading the configuration
        }

        List<String> tasks = parseBuildConfig(buildTool, buildConfig);
        for (String task : tasks) {
            executeTask(buildTool, task);
        }
    }

    private static String determineBuildTool() {
        // Enhanced build tool identification
        if (new File("pom.xml").exists()) {
            return "Maven";
        } else if (new File("build.gradle").exists() || new File("build.gradle.kts").exists()) {
            return "Gradle";
        } else if (new File("build.xml").exists()) {
            return "Ant";
        } else {
            System.err.println("No recognized build tool configuration found.");
            return "";
        }
    }

    private static String readBuildConfig(String buildTool) {
        String configFilePath = "";
        if (buildTool.equalsIgnoreCase("Maven")) {
            configFilePath = "pom.xml";
        } else if (buildTool.equalsIgnoreCase("Gradle")) {
            configFilePath = new File("build.gradle").exists() ? "build.gradle" : "build.gradle.kts";
        } else if (buildTool.equalsIgnoreCase("Ant")) {
            configFilePath = "build.xml";
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(configFilePath));
            StringBuilder configContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                configContent.append(line).append("\n");
            }
            reader.close();
            return configContent.toString();
        } catch (IOException e) {
            System.err.println("Error reading build configuration file: " + e.getMessage());
            return ""; 
        }
    }

    private static List<String> parseBuildConfig(String buildTool, String buildConfig) {
        List<String> tasks = new ArrayList<>();

        if (buildTool.equalsIgnoreCase("Maven")) {
            // Enhanced Maven parsing using regex
            Pattern pattern = Pattern.compile("<goal>([^<]+)</goal>");
            Matcher matcher = pattern.matcher(buildConfig);
            while (matcher.find()) {
                tasks.add(matcher.group(1));
            }
        } else if (buildTool.equalsIgnoreCase("Gradle")) {
            // Enhanced Gradle parsing using regex (handles both Groovy and Kotlin DSL)
            Pattern pattern = Pattern.compile("task\\s+([\\w\\-]+)");
            Matcher matcher = pattern.matcher(buildConfig);
            while (matcher.find()) {
                tasks.add(matcher.group(1));
            }
        } else if (buildTool.equalsIgnoreCase("Ant")) {
            // Enhanced Ant parsing using regex
            Pattern pattern = Pattern.compile("<target\\s+name=\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(buildConfig);
            while (matcher.find()) {
                tasks.add(matcher.group(1));
            }
        }

        return tasks;
    }

    private static void executeTask(String buildTool, String task) {
        // Placeholder for task execution
        System.out.println("Executing " + buildTool + " task: " + task);

        // In a real implementation, you'd likely use ProcessBuilder to execute
        // the actual build tool commands, handling input/output and error streams.
    }
}