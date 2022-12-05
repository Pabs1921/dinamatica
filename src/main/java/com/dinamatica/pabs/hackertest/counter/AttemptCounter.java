package com.dinamatica.pabs.hackertest.counter;

import com.dinamatica.pabs.hackertest.model.LogEntry;

public interface AttemptCounter {

    boolean hasTooManyFailedAttempts(LogEntry logEntry);

    void init();

    int getAttemptCacheSize();
}
