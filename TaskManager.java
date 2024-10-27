/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

import java.util.logging.*;

public class TaskManager {

    private static final Logger logger = Logger.getLogger(TaskManager.class.getName());

    public void executeTask(String taskName) {
        logger.info("Executing task: " + taskName);
        switch (taskName.toLowerCase()) {
            case "analyze data":
                logger.info("Performing data analysis...");
                break;
            case "generate report":
                logger.info("Generating report...");
                break;
            default:
                logger.warning("Unknown task: " + taskName);
        }
    }
}
