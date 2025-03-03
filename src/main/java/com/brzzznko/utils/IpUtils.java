package com.brzzznko.utils;

public final class IpUtils {

    private IpUtils() {}
    public static long ipToInt(String ip) {
        String[] parts = ip.split("\\.");
        return (Long.parseLong(parts[0]) << 24) |
                (Long.parseLong(parts[1]) << 16) |
                (Long.parseLong(parts[2]) << 8) |
                (Long.parseLong(parts[3]));
    }
}
