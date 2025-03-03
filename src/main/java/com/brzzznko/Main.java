package com.brzzznko;

import com.brzzznko.processors.ParallelProcessor;
import com.brzzznko.trackers.AtomicBitArrayIpTracker;
import com.brzzznko.utils.LoadingIndicator;
import com.brzzznko.utils.ProfilerUtils;

import static com.brzzznko.utils.ProfilerUtils.getUsedMemory;

public class Main {
    private static final String DEFAULT_FILE_NAME = "mid-list.txt";

    public static void main(String... args) {
        String filePath = (args.length > 0) ? args[0] : DEFAULT_FILE_NAME;
        System.out.println("Processing file: " + filePath);

        runProcessing(filePath);
    }

    private static void runProcessing(String filePath) {
        // Start progress indicator thread
        LoadingIndicator indicator = new LoadingIndicator();
        Thread loaderThread = new Thread(indicator);
        loaderThread.start();

        // Measure start time and memory usage
        long startTime = System.nanoTime();
        long startMemory = getUsedMemory();

        IpProcessor processor = new ParallelProcessor(new AtomicBitArrayIpTracker());

        try {
            processor.process(filePath);
        } catch (Exception e) {
            System.err.println("Error during processing: " + e.getMessage());
            e.printStackTrace();
        } finally {
            indicator.stop();

            try {
                loaderThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Measure end time and memory usage
        long endTime = System.nanoTime();
        long endMemory = ProfilerUtils.getUsedMemory();

        printStatistics(processor.getCounter(), startTime, endTime, startMemory, endMemory);
    }

    private static void printStatistics(long uniqueCount, long startTime, long endTime, long startMemory, long endMemory) {
        System.out.println("Unique IP's count: " + uniqueCount);
        System.out.printf("Processing Time: %.2f seconds\n", (endTime - startTime) / 1_000_000_000.0);
        System.out.printf("Memory Used: %.2f MB\n", (endMemory - startMemory) / (1024.0 * 1024.0));
    }
}

