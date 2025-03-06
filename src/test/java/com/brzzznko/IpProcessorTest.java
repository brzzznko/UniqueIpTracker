package com.brzzznko;

import com.brzzznko.processors.ProcessorFactory;
import com.brzzznko.processors.ProcessorFactory.ProcessorType;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IpProcessorTest {

    @ParameterizedTest
    @EnumSource(ProcessorType.class)
    void shouldProcessLargeFileCorrectly(ProcessorType type) {
        Path filePath = Paths.get("src/test/resources/test.txt");
        IpProcessor processor = ProcessorFactory.createProcessor(type);

        processor.process(filePath.toString());

        assertEquals(541, processor.getCounter(), "Should correctly process large files");
    }

    @ParameterizedTest
    @EnumSource(ProcessorType.class)
    void shouldHandleEmptyFile(ProcessorType type, @TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve("empty.txt");
        Files.createFile(tempFile);
        IpProcessor processor = ProcessorFactory.createProcessor(type);

        processor.process(tempFile.toString());

        assertEquals(0, processor.getCounter(), "Should not count any IPs in an empty file");
    }

    @ParameterizedTest
    @EnumSource(ProcessorType.class)
    void shouldThrowRuntimeExceptionOnFileError(ProcessorType type) {
        String invalidFilePath = "non_existent_file.txt";
        IpProcessor processor = ProcessorFactory.createProcessor(type);

        assertThrows(RuntimeException.class, () -> processor.process(invalidFilePath));
    }
}
