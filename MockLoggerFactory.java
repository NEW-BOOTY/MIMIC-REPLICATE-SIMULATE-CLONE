/** 
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */

public class MockLoggerFactory {
    public static MockLogger getLogger(Class<?> clazz) {
        return new ConsoleMockLogger(clazz.getName());
    }
}
