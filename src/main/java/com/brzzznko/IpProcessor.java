package com.brzzznko;

public interface IpProcessor {

    void process(String filename);

    int getCounter();

    default String extractIp(String line) {
        return line.split(" ")[0].trim();
    }

}
