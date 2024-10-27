/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

import java.io.IOException;
import java.util.logging.*;

public class LoggerSetup {

    public static void configureLogger() {
        Logger logger = Logger.getLogger("");
        Handler consoleHandler = new ConsoleHandler();
        Handler fileHandler;

        try {
            fileHandler = new FileHandler("dukeai.log", true);
            consoleHandler.setLevel(Level.ALL);
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(consoleHandler);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error setting up logger", e);
        }
    }
}
