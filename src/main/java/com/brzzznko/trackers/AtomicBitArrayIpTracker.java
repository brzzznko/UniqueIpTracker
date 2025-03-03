package com.brzzznko.trackers;

import com.brzzznko.IpTracker;
import com.brzzznko.utils.IpUtils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLongArray;

public class AtomicBitArrayIpTracker implements IpTracker {

    private static final int BIT_ARRAY_SIZE = 1 << 26;
    private final AtomicLongArray bitArray = new AtomicLongArray(BIT_ARRAY_SIZE);
    private final AtomicInteger counter = new AtomicInteger(0);

    public void track(String ip) {
        var number = IpUtils.ipToInt(ip);

        int index = (int) (number / 64);
        long position = 1L << (number % 64);

        while (true) {
            long oldValue = bitArray.get(index);
            if ((oldValue & position) != 0) return; // IP already tracked

            long newValue = oldValue | position;
            if (bitArray.compareAndSet(index, oldValue, newValue)) {
                counter.incrementAndGet();
                break;
            }
        }
    }

    public int getCounter() {
        return counter.get();
    }
}
