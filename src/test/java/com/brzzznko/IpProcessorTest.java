package com.brzzznko;

import com.brzzznko.processors.SimpleProcessor;
import com.brzzznko.processors.ParallelProcessor;
import com.brzzznko.trackers.AtomicBitArrayIpTracker;
import com.brzzznko.trackers.BitArrayIpTracker;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IpProcessorTest {
    private static Stream<IpProcessor> provideIpProcessors() {
        return Stream.of(
                new SimpleProcessor(new BitArrayIpTracker()),
                new ParallelProcessor(new AtomicBitArrayIpTracker())
        );
    }

    @ParameterizedTest
    @MethodSource("provideIpProcessors")
    void shouldProcessLargeFileCorrectly(IpProcessor processor) {
        Path filePath = Paths.get("src/test/resources/test.txt");

        processor.process(filePath.toString());

        assertEquals(541, processor.getCounter(), "Should correctly process large files");
    }

    @ParameterizedTest
    @MethodSource("provideIpProcessors")
    void shouldHandleEmptyFile(IpProcessor processor, @TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve("empty.txt");
        Files.createFile(tempFile);

        processor.process(tempFile.toString());

        assertEquals(0, processor.getCounter(), "Should not count any IPs in an empty file");
    }

    @ParameterizedTest
    @MethodSource("provideIpProcessors")
    void shouldThrowRuntimeExceptionOnFileError(IpProcessor processor) {
        String invalidFilePath = "non_existent_file.txt";

        assertThrows(RuntimeException.class, () -> processor.process(invalidFilePath));
    }
}
