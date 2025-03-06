package com.brzzznko.processors;

import com.brzzznko.IpProcessor;
import com.brzzznko.trackers.AtomicBitArrayIpTracker;
import com.brzzznko.trackers.BitArrayIpTracker;

public class ProcessorFactory {
    
    public enum ProcessorType {
        PARALLEL_STREAMS("parallel-streams"),
        CHUNKS("chunks"),
        SINGLE_THREADED("single-threaded");

        private final String value;

        ProcessorType(String value) {
            this.value = value;
        }

        public static ProcessorType fromString(String text) {
            for (ProcessorType type : ProcessorType.values()) {
                if (type.value.equalsIgnoreCase(text)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown processor type: " + text);
        }
    }

    public static IpProcessor createProcessor(String processorType) {
        try {
            ProcessorType type = ProcessorType.fromString(processorType);
            return createProcessor(type);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid processor type. Using default parallel-streams processor.");
            return createProcessor(ProcessorType.PARALLEL_STREAMS);
        }
    }

    public static IpProcessor createProcessor(ProcessorType type) {
        return switch (type) {
            case PARALLEL_STREAMS -> new ParallelProcessor(new AtomicBitArrayIpTracker());
            case CHUNKS -> new ConcurrentChunkProcessor(new AtomicBitArrayIpTracker());
            case SINGLE_THREADED -> new SingleThreadedProcessor(new BitArrayIpTracker());
        };
    }
} 