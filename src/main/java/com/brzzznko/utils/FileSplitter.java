package com.brzzznko.utils;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FileSplitter {

    private static final long CHUNK_SIZE = 10L * 1024 * 1024 * 1024; // 10GB chunks

    public static List<Path> splitFile(String filename) throws IOException, InterruptedException {
        Path inputPath = Paths.get(filename);
        Path outputDir = Optional.ofNullable(inputPath.getParent()).orElse(Paths.get(""));

        long fileSize = Files.size(inputPath);
        int numChunks = (int) Math.ceil((double) fileSize / CHUNK_SIZE);

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        List<Future<Path>> futures = IntStream.range(0, numChunks)
                .mapToObj(i -> executor.submit(() -> splitChunk(inputPath, outputDir.toString(), i)))
                .toList();

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        return futures.stream().map(f -> {
            try {
                return f.get();
            } catch (Exception e) {
                throw new RuntimeException("Error retrieving chunk file", e);
            }
        }).collect(Collectors.toList());
    }

    private static Path splitChunk(Path inputPath, String outputDir, int chunkIndex) throws IOException {
        Path outputPath = Paths.get(outputDir, "split_chunk_" + chunkIndex + ".txt");

        try (RandomAccessFile file = new RandomAccessFile(inputPath.toFile(), "r");
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outputPath.toFile()))) {

            long startByte = chunkIndex * CHUNK_SIZE;
            long endByte = Math.min(startByte + CHUNK_SIZE, file.length());

            // Move start position to the next newline (`\n`)
            if (startByte > 0) {
                file.seek(startByte);
                while (file.read() != '\n') {
                    startByte++;  // Move forward until we reach a newline
                    if (startByte >= endByte) return outputPath; // If we hit end of chunk, return
                }
            }

            // Start reading from the adjusted start position
            file.seek(startByte);
            byte[] buffer = new byte[1024 * 1024]; // Read 1MB at a time
            int bytesRead;
            long bytesWritten = 0;

            while (bytesWritten < CHUNK_SIZE && (bytesRead = file.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                bytesWritten += bytesRead;
            }

            // Move end position to the next newline (`\n`)
            int lastByte;
            while ((lastByte = file.read()) != -1) {
                out.write(lastByte);
                if (lastByte == '\n') break;
            }
        }

        return outputPath;
    }
}
