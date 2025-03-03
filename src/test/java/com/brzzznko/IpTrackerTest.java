package com.brzzznko;

import com.brzzznko.trackers.AtomicBitArrayIpTracker;
import com.brzzznko.trackers.BitArrayIpTracker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IpTrackerTest {

    private static Stream<IpTracker> provideIpTrackers() {
        return Stream.of(
                new BitArrayIpTracker(),
                new AtomicBitArrayIpTracker()
        );
    }

    @ParameterizedTest
    @MethodSource("provideIpTrackers")
    void shouldTrackUniqueIpsCorrectly(IpTracker ipTracker) {
        ipTracker.track("192.168.1.1");
        ipTracker.track("10.0.0.1");
        ipTracker.track("172.16.0.1");

        assertEquals(3, ipTracker.getCounter(), "Should track 3 unique IPs");
    }

    @ParameterizedTest
    @MethodSource("provideIpTrackers")
    void shouldNotIncrementCounterForDuplicateIps(IpTracker ipTracker) {
        ipTracker.track("192.168.1.1");
        ipTracker.track("192.168.1.1"); // Duplicate
        ipTracker.track("10.0.0.1");
        ipTracker.track("10.0.0.1"); // Duplicate

        assertEquals(2, ipTracker.getCounter(), "Should track only 2 unique IPs");
    }

    @ParameterizedTest
    @MethodSource("provideIpTrackers")
    void shouldTrackBoundaryIps(IpTracker ipTracker) {
        ipTracker.track("0.0.0.0");
        ipTracker.track("255.255.255.255");

        assertEquals(2, ipTracker.getCounter(), "Should correctly track boundary IPs");
    }
}
