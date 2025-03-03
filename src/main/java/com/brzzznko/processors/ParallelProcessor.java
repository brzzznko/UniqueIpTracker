package com.brzzznko.processors;

import com.brzzznko.IpProcessor;
import com.brzzznko.IpTracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ParallelProcessor implements IpProcessor {

    private final IpTracker tracker;

    public ParallelProcessor(IpTracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public void process(String filename) {
        try (Stream<String> lines = Files.lines(Paths.get(filename)).parallel()) {
            lines.forEach(line -> tracker.track(line.split(" ")[0].trim()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getCounter() {
        return tracker.getCounter();
    }

}
