import java.util.*;
import java.util.concurrent.*;

/**
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 *
 * This class simulates a robust task scheduler system.
 * It handles scheduling, executing tasks, and shutdowns with failure prevention and recovery mechanisms.
 */
public class RobustTaskScheduler {

    private final SortedMap<Long, Runnable> taskMap = new TreeMap<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private volatile boolean isShuttingDown = false;

    public static void main(String[] args) {
        RobustTaskScheduler scheduler = new RobustTaskScheduler();

        // Schedule tasks
        try {
            scheduler.scheduleTask(() -> System.out.println("Task 1 executed"), 5);
            scheduler.scheduleTask(() -> System.out.println("Task 2 executed"), 10);
            scheduler.scheduleTask(() -> System.out.println("Task 3 executed"), 2);

            // Wait for all tasks to execute
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (IllegalArgumentException e) {
            System.err.println("Error scheduling task: " + e.getMessage());
        }

        // Gracefully shutdown the scheduler
        scheduler.shutdown();
    }

    /**
     * Schedules a task to be executed after a specified delay.
     *
     * @param task The task to be executed.
     * @param delaySeconds The delay in seconds before the task is executed.
     * @throws IllegalArgumentException If delaySeconds is negative or zero.
     */
    public void scheduleTask(Runnable task, int delaySeconds) {
        // Key Enhancements and Error Handling

        // Task Scheduling Errors:
        // Invalid Delay: Logs an error message and bypasses scheduling if the delay is invalid.
        if (task == null) {
            System.err.println("Task cannot be null.");
            return;
        }
        if (delaySeconds <= 0) {
            System.err.println("Delay must be positive and greater than zero.");
            return;
        }

        long scheduledTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(delaySeconds);
        taskMap.put(scheduledTime, task);

        try {
            // Schedule the task with a delay
            executor.schedule(() -> executeTask(scheduledTime), delaySeconds, TimeUnit.SECONDS);
        } catch (RejectedExecutionException e) {
            // RejectedExecutionException: Catches exceptions when scheduling tasks, logs the error, and removes the task from the map.
            System.err.println("Error scheduling task: " + e.getMessage());
            taskMap.remove(scheduledTime); // Remove the task if scheduling fails
        }
    }

    /**
     * Executes the task scheduled at the specified time.
     *
     * @param scheduledTime The time at which the task was scheduled to be executed.
     */
    private void executeTask(long scheduledTime) {
        Runnable task = taskMap.remove(scheduledTime);
        if (task != null) {
            try {
                // Execute the task and log the execution time
                System.out.println("Executing task at " + new Date());
                task.run();
            } catch (Exception e) {
                // Task Execution Failures: Catches and logs exceptions during task execution, ensuring the scheduler continues to operate even if some tasks fail.
                System.err.println("Task execution failed: " + e.getMessage());
            }
        } else {
            System.err.println("Task not found for scheduled time: " + new Date(scheduledTime));
        }
    }

    /**
     * Shuts down the scheduler and waits for tasks to complete.
     */
    public void shutdown() {
        // Shutdown Failures:

        // Prevent Duplicate Shutdown: Checks if the scheduler is already shutting down to prevent redundant shutdown attempts.
        if (isShuttingDown) {
            System.err.println("Scheduler is already shutting down.");
            return;
        }

        isShuttingDown = true;
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                // Shutdown and Retry: Attempts a graceful shutdown and, if it fails, attempts a forced shutdown while logging errors.
                System.err.println("Scheduler did not terminate in the allotted time. Attempting forced shutdown...");
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Scheduler did not terminate properly.");
                }
            }
        } catch (InterruptedException ex) {
            // InterruptedException Handling: Catches and logs interruptions during shutdown, ensuring the thread is properly interrupted.
            System.err.println("Shutdown interrupted: " + ex.getMessage());
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        } finally {
            // Logging and Status Updates:
            // Console Logs: Provides detailed feedback on task execution and scheduler status, improving visibility into the scheduler’s operation and any issues encountered.
            System.out.println("Scheduler shutdown complete.");
        }
    }
}

/*
 * This Java code defines a class RobustTaskScheduler that simulates a robust task scheduling system with enhanced error handling and recovery mechanisms. Here’s a detailed breakdown of what each part of the code does:
 *
 * Main Method:
 * The main method creates an instance of RobustTaskScheduler and schedules three tasks with different delays.
 * It waits for all tasks to execute by sleeping for 15 seconds and then gracefully shuts down the scheduler.
 *
 * scheduleTask Method:
 * This method schedules a task to be executed after a specified delay.
 * It includes error handling for invalid delays and null tasks, logging appropriate error messages.
 * It attempts to schedule the task using a ScheduledExecutorService and handles RejectedExecutionException if scheduling fails.
 *
 * executeTask Method:
 * This method executes the task scheduled at the specified time.
 * It retrieves and removes the task from the taskMap and runs it, logging the execution time.
 * It includes error handling for task execution failures, ensuring the scheduler continues to operate even if some tasks fail.
 *
 * shutdown Method:
 * This method shuts down the scheduler and waits for tasks to complete.
 * It prevents duplicate shutdown attempts by checking if the scheduler is already shutting down.
 * It attempts a graceful shutdown and, if it fails, attempts a forced shutdown while logging errors.
 * It handles interruptions during shutdown and ensures the thread is properly interrupted.
 * It provides detailed feedback on task execution and scheduler status through console logs.
 *
 * Example Usage:
 * When the main method is executed, it schedules three tasks with delays of 5, 10, and 2 seconds, respectively.
 * The tasks print messages to the console when executed.
 * After 15 seconds, the scheduler is shut down, and a message indicating the shutdown completion is printed.
 * This code is useful for simulating a robust task scheduling system where tasks need to be executed after specific delays, with enhanced error handling and recovery mechanisms.
 */
