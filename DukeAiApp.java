/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

import javax.security.auth.login.*;
import java.util.Scanner;
import java.util.logging.*;

public class DukeAiApp {

    private static final Logger logger = Logger.getLogger(DukeAiApp.class.getName());
    private static final TaskManager taskManager = new TaskManager();
    private static final AuthModule authModule = new AuthModule();

    public static void main(String[] args) {
        LoggerSetup.configureLogger();
        logger.info("DUKEAi Application Starting...");

        if (authenticateUser()) {
            logger.info("User successfully authenticated.");
            displayMenu();
            handleUserCommands();
        } else {
            logger.severe("Authentication failed. Exiting application.");
        }
    }

    private static boolean authenticateUser() {
        try {
            LoginContext loginContext = new LoginContext("DukeAiLogin", new AuthModule.MyCallbackHandler());
            loginContext.login();
            return true;
        } catch (LoginException e) {
            logger.log(Level.SEVERE, "Authentication Error: ", e);
            return false;
        }
    }

    private static void displayMenu() {
        System.out.println("Welcome to DUKEAi!");
        System.out.println("1. Execute Task");
        System.out.println("2. Read File");
        System.out.println("3. Write File");
        System.out.println("4. Exit");
    }

    private static void handleUserCommands() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.print("Enter command (1-4): ");
            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    System.out.print("Enter task name: ");
                    String taskName = scanner.nextLine();
                    taskManager.executeTask(taskName);
                    break;
                case "2":
                    System.out.print("Enter file path to read: ");
                    String filePath = scanner.nextLine();
                    FileHandler.readFile(filePath);
                    break;
                case "3":
                    System.out.print("Enter file path to write: ");
                    filePath = scanner.nextLine();
                    System.out.print("Enter content to write: ");
                    String content = scanner.nextLine();
                    FileHandler.writeFile(filePath, content);
                    break;
                case "4":
                    exit = true;
                    logger.info("Exiting application...");
                    break;
                default:
                    logger.warning("Invalid command entered.");
                    System.out.println("Invalid command. Please try again.");
            }
        }
        scanner.close();
    }
}
