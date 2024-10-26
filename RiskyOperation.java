// Copyright (c) 2024 Devin B. Royal
// This Java code performs the same operations as the provided Python code. It attempts to execute a risky operation that could throw an exception,
// catches specific exceptions, and includes a 'finally' block to ensure certain actions are always taken, such as cleanup.

public class RiskyOperation {
    public static void main(String[] args) {
        performRiskyOperation();
    }

    public static void performRiskyOperation() {
        try {
            // Code that might throw an exception
            int result = 1 / 0;
        } catch (ArithmeticException e) {
            System.out.println("ArithmeticException occurred: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            // Code that will always execute, regardless of whether an exception was thrown or not
            System.out.println("Cleanup code executed");
        }
    }
}
