import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 *
 * This class combines functionality to mimic command execution and a robust task scheduler.
 * It handles command execution, task scheduling, and graceful shutdowns with advanced error prevention mechanisms.
 */
public class CombinedSystem {

    private static final Logger logger = LoggerFactory.getLogger(CombinedSystem.class);
    private final PriorityBlockingQueue<ScheduledTask> taskQueue = new PriorityBlockingQueue<>();
    private final ScheduledThreadPoolExecutor executor;
    private final AtomicBoolean isShuttingDown = new AtomicBoolean(false);
    private final Properties config = new Properties();
    private final RateLimiter rateLimiter;
    private final Lock taskLock = new ReentrantLock();
    private final long configurationReloadInterval = 60000; // 1 minute
    private volatile long lastConfigReloadTime = 0;

    static {
        // Configure SLF4J with a simple logger by setting system properties
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG");
        System.setProperty("org.slf4j.simpleLogger.showThreadName", "true");
        System.setProperty("org.slf4j.simpleLogger.showLogName", "true");
        System.setProperty("org.slf4j.simpleLogger.showShortLogName", "false");
    }

    public CombinedSystem(int initialPoolSize) {
        this.executor = new ScheduledThreadPoolExecutor(initialPoolSize, new CustomThreadFactory());
        this.rateLimiter = RateLimiter.create(getRateLimit());
        loadConfiguration();
        startConfigReloadScheduler();
    }

    public static void main(String[] args) {
        CombinedSystem system = new CombinedSystem(2); // Example initial pool size

        // Key Features:
        // Command Execution: Secure execution of system commands with basic injection prevention.
        String command = "echo Hello, World!"; // Example command; replace with actual command
        try {
            Process process = executeCommand(command);
            if (process != null) {
                handleProcessOutput(process);
            }
        } catch (IOException e) {
            logger.error("Error executing command: ", e);
        }

        // Schedule tasks with dependencies
        try {
            Runnable task1 = () -> System.out.println("Task 1 executed");
            Runnable task2 = () -> System.out.println("Task 2 executed");
            Runnable task3 = () -> System.out.println("Task 3 executed");

            system.scheduleTask(task1, 5, null, 1); // Task 1 has no dependencies
            system.scheduleTask(task2, 10, Arrays.asList(task1), 2); // Task 2 depends on Task 1
            system.scheduleTask(task3, 2, null, 0); // Task 3 has no dependencies

            // Wait for all tasks to execute
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            logger.error("Main thread interrupted: ", e);
            Thread.currentThread().interrupt();
        } catch (IllegalArgumentException e) {
            logger.error("Error scheduling task: ", e);
        }

        // Gracefully shutdown the scheduler
        system.shutdown();
    }

    /**
     * Loads and validates configuration from the properties file.
     */
    private void loadConfiguration() {
        try (InputStream input = new FileInputStream("config.properties")) {
            config.load(input);
            String poolSize = config.getProperty("thread.pool.size", "2");
            int rateLimit = Integer.parseInt(config.getProperty("rate.limit", "10"));
            validateConfiguration(poolSize, rateLimit);
            executor.setCorePoolSize(Integer.parseInt(poolSize));
            rateLimiter.updateRateLimit(rateLimit);
            logger.info("Configuration loaded. Thread pool size: " + poolSize + ", Rate limit: " + rateLimit);
        } catch (IOException e) {
            logger.warn("Configuration file not found, using default settings.", e);
        } catch (NumberFormatException e) {
            logger.error("Error parsing configuration values: ", e);
        }
    }

    /**
     * Validates configuration values.
     *
     * @param poolSize The thread pool size.
     * @param rateLimit The rate limit for task execution.
     */
    private void validateConfiguration(String poolSize, int rateLimit) {
        try {
            int size = Integer.parseInt(poolSize);
            if (size <= 0) {
                throw new IllegalArgumentException("Thread pool size must be greater than zero.");
            }
            if (rateLimit <= 0) {
                throw new IllegalArgumentException("Rate limit must be greater than zero.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in configuration.", e);
        }
    }

    /**
     * Gets the rate limit from the configuration.
     *
     * @return The rate limit for task execution.
     */
    private int getRateLimit() {
        String rateLimitProperty = config.getProperty("rate.limit", "10");
        return Integer.parseInt(rateLimitProperty);
    }

    /**
     * Starts a scheduler that reloads the configuration at regular intervals.
     */
    private void startConfigReloadScheduler() {
        executor.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastConfigReloadTime > configurationReloadInterval) {
                loadConfiguration();
                lastConfigReloadTime = currentTime;
            }
        }, configurationReloadInterval, configurationReloadInterval, TimeUnit.MILLISECONDS);
    }

    /**
     * Executes a system command and returns the Process object.
     *
     * @param command The command to execute.
     * @return The Process object representing the command execution.
     * @throws IOException If an I/O error occurs.
     */
    private static Process executeCommand(String command) throws IOException {
        // Prevent command injection by sanitizing input (basic example)
        if (command.contains("&&") || command.contains(";")) {
            throw new SecurityException("Command contains potential injection characters.");
        }
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectErrorStream(true); // Combine stdout and stderr
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

            // Ensure the process has exited successfully
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                logger.error("Process exited with error code: " + exitCode);
            }

        } catch (IOException e) {
            logger.error("Error reading process output: ", e);
        } catch (InterruptedException e) {
            logger.error("Process interrupted: ", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Schedules a task to be executed after a specified delay.
     *
     * @param task The task to be executed.
     * @param delaySeconds The delay in seconds before the task is executed.
     * @param dependencies List of dependent tasks that need to complete before this task.
     * @param priority The priority of the task.
     * @throws IllegalArgumentException If delaySeconds is negative or zero.
     */
    public void scheduleTask(Runnable task, int delaySeconds, List<Runnable> dependencies, int priority) {
        if (task == null) {
            logger.error("Task cannot be null.");
            return;
        }
        if (delaySeconds <= 0) {
            logger.error("Delay must be positive and greater than zero.");
            return;
        }

        long scheduledTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(delaySeconds);

        taskLock.lock();
        try {
            // Handle dependencies
            if (dependencies != null && !dependencies.isEmpty()) {
                for (Runnable dependency : dependencies) {
                    if (dependency == null) {
                        logger.error("Dependency cannot be null.");
                        return;
                    }
                    // Check if dependencies are already executed (dummy implementation)
                    if (!taskQueue.contains(new ScheduledTask(dependency, 0, 0))) {
                        logger.error("Dependency not found: " + dependency);
                        return;
                    }
                }
                logger.info("Task dependencies handled.");
            }

            rateLimiter.acquire(); // Enforce rate limit

            ScheduledTask scheduledTask = new ScheduledTask(task, scheduledTime, priority);
            taskQueue.offer(scheduledTask);
            logger.info("Task scheduled with priority " + priority + " at " + new Date(scheduledTime));
            processScheduledTasks();

        } finally {
            taskLock.unlock();
        }
    }

    /**
     * Processes tasks from the queue and executes them.
     */
    private void processScheduledTasks() {
        while (!taskQueue.isEmpty() && !isShuttingDown.get()) {
            ScheduledTask scheduledTask = taskQueue.peek();
            if (scheduledTask == null) break;

            if (scheduledTask.getScheduledTime() <= System.currentTimeMillis()) {
                taskQueue.poll(); // Remove from queue
                Runnable task = scheduledTask.getTask();
                logger.info("Executing task with priority " + scheduledTask.getPriority() + " at " + new Date());
                CompletableFuture.runAsync(task, executor)
                        .exceptionally(ex -> {
                            logger.error("Task execution failed: ", ex);
                            return null;
                        });
            } else {
                break;
            }
        }
    }

    /**
     * Shuts down the scheduler gracefully.
     */
    public void shutdown() {
        if (isShuttingDown.getAndSet(true)) {
            logger.warn("Scheduler is already shutting down.");
            return;
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                logger.warn("Scheduler did not terminate in the allotted time. Attempting forced shutdown...");
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    logger.error("Scheduler did not terminate properly.");
                }
            }
        } catch (InterruptedException e) {
            logger.error("Shutdown interrupted: ", e);
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        } finally {
            logger.info("Scheduler shutdown complete.");
        }
    }

    private static class CustomThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix = "scheduler-thread-";

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, namePrefix + threadNumber.getAndIncrement());
            thread.setDaemon(true); // Daemon threads exit when the JVM shuts down
            return thread;
        }
    }

    private static class ScheduledTask implements Comparable<ScheduledTask> {
        private final Runnable task;
        private final long scheduledTime;
        private final int priority;

        public ScheduledTask(Runnable task, long scheduledTime, int priority) {
            this.task = task;
            this.scheduledTime = scheduledTime;
            this.priority = priority;
        }

        public Runnable getTask() {
            return task;
        }

        public long getScheduledTime() {
            return scheduledTime;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public int compareTo(ScheduledTask other) {
            // Prioritize tasks by their scheduled time and priority
            int timeComparison = Long.compare(this.scheduledTime, other.scheduledTime);
            if (timeComparison != 0) {
                return timeComparison;
            }
            return Integer.compare(other.priority, this.priority); // Higher priority first
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ScheduledTask that = (ScheduledTask) o;
            return scheduledTime == that.scheduledTime && priority == that.priority && Objects.equals(task, that.task);
        }

        @Override
        public int hashCode() {
            return Objects.hash(task, scheduledTime, priority);
        }
    }

    // Inner class RateLimiter and its methods as shown previously
    private static class RateLimiter {
        private long rateLimit;
        private long lastExecutionTime;

        private RateLimiter(long rateLimit) {
            this.rateLimit = rateLimit;
            this.lastExecutionTime = System.currentTimeMillis();
        }

        public static RateLimiter create(long rateLimit) {
            return new RateLimiter(rateLimit);
        }

        public synchronized void acquire() {
            long currentTime = System.currentTimeMillis();
            long timeElapsed = currentTime - lastExecutionTime;

            if (timeElapsed < rateLimit) {
                try {
                    Thread.sleep(rateLimit - timeElapsed);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            lastExecutionTime = System.currentTimeMillis();
        }

        public synchronized void updateRateLimit(long newRateLimit) {
            this.rateLimit = newRateLimit;
            logger.info("Rate limit updated to: " + newRateLimit);
        }
    }
}
