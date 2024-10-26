import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 *
 * This class combines functionality to mimic command execution and a robust task scheduler.
 * It handles command execution, task scheduling, and graceful shutdowns with error prevention mechanisms.
 */
public class CombinedSystem {

    private final SortedMap<Long, Runnable> taskMap = new TreeMap<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private volatile boolean isShuttingDown = false;

    public static void main(String[] args) {
        CombinedSystem system = new CombinedSystem();

        // Mimic command execution
        String command = "echo Hello, World!"; // Example command; replace with actual command
        try {
            Process process = executeCommand(command);
            if (process != null) {
                handleProcessOutput(process);
            }
        } catch (IOException e) {
            System.err.println("Error executing command: " + e.getMessage());
        }

        // Schedule tasks
        try {
            system.scheduleTask(() -> System.out.println("Task 1 executed"), 5);
            system.scheduleTask(() -> System.out.println("Task 2 executed"), 10);
            system.scheduleTask(() -> System.out.println("Task 3 executed"), 2);

            // Wait for all tasks to execute
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (IllegalArgumentException e) {
            System.err.println("Error scheduling task: " + e.getMessage());
        }

        // Gracefully shutdown the scheduler
        system.shutdown();
    }

    /**
     * Executes a system command and returns the Process object.
     *
     * @param command The command to execute.
     * @return The Process object representing the command execution.
     * @throws IOException If an I/O error occurs.
     */
    private static Process executeCommand(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        return processBuilder.start();
    }

    /**
     * Handles the output of a process.
     *
     * @param process The Process object from which to read the output.
     */
    private static void handleProcessOutput(Process process) {
        try (InputStream inputStream = process.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            System.err.println("Error reading process output: " + e.getMessage());
        }
    }

    /**
     * Schedules a task to be executed after a specified delay.
     *
     * @param task The task to be executed.
     * @param delaySeconds The delay in seconds before the task is executed.
     * @throws IllegalArgumentException If delaySeconds is negative or zero.
     */
    public void scheduleTask(Runnable task, int delaySeconds) {
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
            executor.schedule(() -> executeTask(scheduledTime), delaySeconds, TimeUnit.SECONDS);
        } catch (RejectedExecutionException e) {
            System.err.println("Error scheduling task: " + e.getMessage());
            taskMap.remove(scheduledTime);
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
                System.out.println("Executing task at " + new Date());
                task.run();
            } catch (Exception e) {
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
        if (isShuttingDown) {
            System.err.println("Scheduler is already shutting down.");
            return;
        }

        isShuttingDown = true;
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.err.println("Scheduler did not terminate in the allotted time. Attempting forced shutdown...");
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Scheduler did not terminate properly.");
                }
            }
        } catch (InterruptedException ex) {
            System.err.println("Shutdown interrupted: " + ex.getMessage());
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("Scheduler shutdown complete.");
        }
    }
}

/*
 * This Java code defines a class CombinedSystem that combines functionality to mimic command execution and a robust task scheduler. Here’s a detailed breakdown of what each part of the code does:
 *
 * Main Method:
 * The main method creates an instance of CombinedSystem and performs two main tasks:
 * Mimic Command Execution: Executes a system command (echo Hello, World!) and handles its output.
 * Schedule Tasks: Schedules three tasks with different delays and waits for all tasks to execute by sleeping for 15 seconds. Finally, it gracefully shuts down the scheduler.
 *
 * executeCommand Method:
 * This method executes a system command and returns the Process object representing the command execution.
 * It uses ProcessBuilder to start the command.
 *
 * handleProcessOutput Method:
 * This method handles the output of a process.
 * It reads the process’s input stream and prints each line to the console.
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
 * When the main method is executed, it mimics the execution of a system command and prints its output.
 * It schedules three tasks with delays of 5, 10, and 2 seconds, respectively. The tasks print messages to the console when executed.
 * After 15 seconds, the scheduler is shut down, and a message indicating the shutdown completion is printed.
 * This code is useful for combining command execution and task scheduling with enhanced error handling and recovery mechanisms.
 */
