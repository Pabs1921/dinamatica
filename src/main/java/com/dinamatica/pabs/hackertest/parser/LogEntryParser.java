package com.dinamatica.pabs.hackertest.parser;

import com.dinamatica.pabs.hackertest.model.LogEntry;

public interface LogEntryParser {

    LogEntry parse(String line);
}
