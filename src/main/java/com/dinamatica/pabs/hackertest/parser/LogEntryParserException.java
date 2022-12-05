package com.dinamatica.pabs.hackertest.parser;

public class LogEntryParserException extends RuntimeException {

    public LogEntryParserException(String message) {
        super(message);
    }
    public LogEntryParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
