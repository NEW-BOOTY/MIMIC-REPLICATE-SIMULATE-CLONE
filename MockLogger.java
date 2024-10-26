/** 
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */

public interface MockLogger {
    void trace(String message);
    void debug(String message);
    void info(String message);
    void warn(String message);
    void error(String message);
}
