package com.brzzznko.utils;

public final class ProfilerUtils {

    private ProfilerUtils() {}
    public static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
