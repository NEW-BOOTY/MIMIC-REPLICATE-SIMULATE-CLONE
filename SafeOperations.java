/** Copyright © 2024 Devin B. Royal. All Rights Reserved. */

public final class SafeOperations {

    public static void performOperationWithDelay(int maxAttempts, long delay) {
        for (int i = 0; i < maxAttempts; i++) {
            try {
                // Perform the operation
                Thread.sleep(delay); // Introduce a delay between attempts
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupt status
                System.err.println("Operation interrupted: " + e.getMessage());
                return; // Exit the loop if interrupted
            }
        }
    }

    public static void safelyHandleExceptions() {
        try {
            // Example of safe handling
            riskyMethod();
        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        }
    }

    public static void riskyMethod() throws Exception {
        // Example method that could throw an exception
    }

    // Sample usage
    public static void main(String[] args) {
        performOperationWithDelay(5, 5000);
        safelyHandleExceptions();
    }
}
