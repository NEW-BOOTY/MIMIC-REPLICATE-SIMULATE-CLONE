/** 
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */

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
