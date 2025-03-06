package com.brzzznko;

import com.brzzznko.processors.ProcessorFactory;
import com.brzzznko.utils.LoadingIndicator;
import com.brzzznko.utils.ProfilerUtils;
import picocli.CommandLine;

import java.util.concurrent.Callable;

import static com.brzzznko.utils.ProfilerUtils.getUsedMemory;
import static picocli.CommandLine.*;

@Command(name = "unique-ip-tracker", mixinStandardHelpOptions = true, version = "1.0.0",
        description = "Processes large files and counts unique IPs using different processing methods.")

public class Main implements Callable<Integer> {

    private static final String DEFAULT_FILE_NAME = "example.txt";
    private static final String DEFAULT_PROCESSOR = "parallel-streams";

    @Option(names = "--processor", description = "Select processing method: parallel-streams (default), chunks, single-threaded")
    private String processorType = DEFAULT_PROCESSOR;

    @Option(names = "--filename", description = "Path to the input file (default: example.txt)")
    private String filePath = DEFAULT_FILE_NAME;

    public static void main(String... args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        System.out.println("Processing file: " + filePath);
        System.out.println("Using processor: " + processorType);

        runProcessing();
        return 0;
    }

    private void runProcessing() {
        // Start progress indicator thread
        LoadingIndicator indicator = new LoadingIndicator();
        Thread loaderThread = new Thread(indicator);
        loaderThread.start();

        // Measure start time and memory usage
        long startTime = System.currentTimeMillis();
        long startMemory = getUsedMemory();

        IpProcessor processor = ProcessorFactory.createProcessor(processorType);

        try {
            processor.process(filePath);
        } catch (Exception e) {
            System.err.println("Error during processing: " + e.getMessage());
        } finally {
            indicator.stop();

            try {
                loaderThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Measure end time and memory usage
        long endTime = System.currentTimeMillis();
        long endMemory = ProfilerUtils.getUsedMemory();

        printStatistics(processor.getCounter(), startTime, endTime, startMemory, endMemory);
    }

    private static void printStatistics(long uniqueCount, long startTime, long endTime, long startMemory, long endMemory) {
        System.out.println("Unique IP's count: " + uniqueCount);
        System.out.printf("Processing Time: %.2f seconds\n", (endTime - startTime) / 1000.0);
        System.out.printf("Memory Used: %.2f MB\n", (endMemory - startMemory) / (1024.0 * 1024.0));
    }
}

