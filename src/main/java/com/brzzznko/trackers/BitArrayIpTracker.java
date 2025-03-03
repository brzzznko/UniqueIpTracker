package com.brzzznko.trackers;

import com.brzzznko.IpTracker;
import com.brzzznko.utils.IpUtils;

public class BitArrayIpTracker implements IpTracker {

    private static final int BIT_ARRAY_SIZE = 1 << 26;
    private final long[] bitArray = new long[BIT_ARRAY_SIZE];
    private int counter = 0;

    public void track(String ip) {
        var number = IpUtils.ipToInt(ip);

        int index = (int) (number / 64);  // Find the integer index
        long position = (int) (number % 64); // Find the bit position

        if ((bitArray[index] & (1L << position)) == 0) {
            bitArray[index] |= (1L << position);
            counter++;
        }
    }

    public int getCounter() {
        return counter;
    }
}
