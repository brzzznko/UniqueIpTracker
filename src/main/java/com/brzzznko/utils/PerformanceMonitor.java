package com.brzzznko.utils;

import java.time.Duration;
import java.time.Instant;

public class PerformanceMonitor {
    private final Instant startTime;
    private Instant endTime;
    private final long startMemory;
    private long endMemory;
    private long uniqueIps;
    private final String processorType;

    public PerformanceMonitor(String processorType) {
        this.startTime = Instant.now();
        this.startMemory = getUsedMemory();
        this.uniqueIps = 0;
        this.processorType = processorType;
    }

    public static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    public void setUniqueIpsCount(long count) {
        uniqueIps = count;
    }

    public void stop() {
        this.endTime = Instant.now();
        this.endMemory = getUsedMemory();
    }

    public void printStatistics() {
        System.out.println("\nPerformance Statistics:");
        System.out.println("====================");
        System.out.println("Processor Type: " + processorType);
        System.out.println("Processing Time: " + formatDuration(getProcessingTime()));
        System.out.printf("Memory Used: %.2f MB%n", getMemoryUsedMB());
        System.out.println("Unique IPs Found: " + uniqueIps);
        System.out.println("====================\n");
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        long millis = duration.toMillisPart();

        if (hours > 0) {
            return String.format("%dh %dm %ds %dms", hours, minutes, seconds, millis);
        } else if (minutes > 0) {
            return String.format("%dm %ds %dms", minutes, seconds, millis);
        } else if (seconds > 0) {
            return String.format("%ds %dms", seconds, millis);
        } else {
            return String.format("%dms", millis);
        }
    }

    public Duration getProcessingTime() {
        return Duration.between(startTime, endTime);
    }

    public double getMemoryUsedMB() {
        return (endMemory - startMemory) / (1024.0 * 1024.0);
    }
} 