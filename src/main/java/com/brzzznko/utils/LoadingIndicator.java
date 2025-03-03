package com.brzzznko.utils;

import java.util.concurrent.atomic.AtomicBoolean;

public class LoadingIndicator implements Runnable {

    private static final String[] DOTS = {"...", "..", ".", ""};
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    public void stop() {
        isRunning.set(false);
    }

    @Override
    public void run() {

        int i = 0;
        while (isRunning.get()) {
            System.out.print("\rProcessing" + DOTS[i % DOTS.length]);
            i++;

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.print("\rProcessing... Done!   \n");
    }
}
