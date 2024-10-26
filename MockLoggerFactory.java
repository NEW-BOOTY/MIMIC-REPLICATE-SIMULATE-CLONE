/** 
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */
// MockLogger.java
public interface MockLogger {
    void trace(String message);
    void debug(String message);
    void info(String message);
    void warn(String message);
    void error(String message);
}

// MockLoggerFactory.java
public class MockLoggerFactory {
    public static MockLogger getLogger(Class<?> clazz) {
        return new ConsoleMockLogger(clazz.getName());
    }
}

// ConsoleMockLogger.java
public class ConsoleMockLogger implements MockLogger {
    private final String name;

    public ConsoleMockLogger(String name) {
        this.name = name;
    }

    private void log(String level, String message) {
        System.out.println(String.format("[%s] %s: %s", level, name, message));
    }

    @Override
    public void trace(String message) {
        log("TRACE", message);
    }

    @Override
    public void debug(String message) {
        log("DEBUG", message);
    }

    @Override
    public void info(String message) {
        log("INFO", message);
    }

    @Override
    public void warn(String message) {
        log("WARN", message);
    }

    @Override
    public void error(String message) {
        log("ERROR", message);
    }
}

// Usage in your main class
public class Main {
    private static final MockLogger logger = MockLoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Application started.");
        
        try {
            logger.debug("Attempting to execute command...");
            // Simulate command execution
            throw new RuntimeException("Simulated exception");
        } catch (Exception e) {
            logger.error("An error occurred: " + e.getMessage());
        }

        logger.warn("This is a warning message.");
        logger.info("Application finished.");
    }
}
