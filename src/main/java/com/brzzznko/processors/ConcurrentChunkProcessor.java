package com.brzzznko.processors;

import com.brzzznko.IpProcessor;
import com.brzzznko.IpTracker;
import com.brzzznko.utils.FileSplitter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class ConcurrentChunkProcessor implements IpProcessor {

    private final IpTracker tracker;

    public ConcurrentChunkProcessor(IpTracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public void process(String filename) {
        try {
            List<Path> chunkFiles = FileSplitter.splitFile(filename);
            processChunks(chunkFiles);

            for (Path chunkFile : chunkFiles) {
                Files.delete(chunkFile);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getCounter() {
        return tracker.getCounter();
    }

    private void processChunks(List<Path> chunkFiles) throws InterruptedException {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        List<Future<Void>> futures = chunkFiles.stream()
                .map(chunk -> executor.submit(() -> processChunk(chunk)))
                .toList();

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                throw new RuntimeException("Error processing chunk", e);
            }
        }
    }

    private Void processChunk(Path chunkFile) {
        try (Stream<String> lines = Files.lines(chunkFile)) {
            lines.forEach(line -> tracker.track(line.split(" ")[0].trim()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


}
