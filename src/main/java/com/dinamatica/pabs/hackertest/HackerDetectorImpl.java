package com.dinamatica.pabs.hackertest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dinamatica.pabs.hackertest.counter.AttemptCounter;
import com.dinamatica.pabs.hackertest.model.LogEntry;
import com.dinamatica.pabs.hackertest.parser.LogEntryParser;
import com.dinamatica.pabs.hackertest.parser.LogEntryParserImpl;

@Component
public class HackerDetectorImpl implements HackerDetector {

    private LogEntryParser logEntryParser;

    private AttemptCounter attemptCounter;

    @Autowired
    public HackerDetectorImpl(LogEntryParserImpl logEntryParser, AttemptCounter attemptCounter) {
        this.logEntryParser = logEntryParser;
        this.attemptCounter = attemptCounter;
    }

    public String parseLine(String line) {

        LogEntry logEntry = logEntryParser.parse(line);

        if (attemptCounter.hasTooManyFailedAttempts(logEntry)) {
            return logEntry.getIpAddress();
        } else {
            return null;
        }
    }

    public void init() {
        attemptCounter.init();
    }

    public int getAttemptCounterChacheSize() {
        return attemptCounter.getAttemptCacheSize();
    }

}
