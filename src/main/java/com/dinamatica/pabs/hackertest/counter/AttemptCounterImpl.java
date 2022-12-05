package com.dinamatica.pabs.hackertest.counter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dinamatica.pabs.hackertest.model.LogEntry;

import static com.dinamatica.pabs.hackertest.model.LogEntry.LoginAction.SIGNIN_SUCCESS;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AttemptCounterImpl implements AttemptCounter {

    private static Map<String, FailedLoginAttempts> failedLoginAttemptsByIp = new ConcurrentHashMap<>();

    @Value("${hackerdetector.timeWindowInMinutes}")
    private int timeWindowInMinutes;

    @Value("${hackerdetector.maxAttempts}")
    private int maxAttempts;

    @Override
    public boolean hasTooManyFailedAttempts(LogEntry logEntry) {

        if (SIGNIN_SUCCESS.equals(logEntry.getLoginAction())) {
            return false;
        }

        synchronized (this) {
            removeOldAttempts(logEntry);

            FailedLoginAttempts failedLoginAttempts = getFailedLoginAttemptsForIp(logEntry);

            int failedAttemptsCount = failedLoginAttempts.getFailedAttemptsCountWithinTimeWindow(logEntry.getDateTime(), timeWindowInMinutes);
            return failedAttemptsCount >= maxAttempts;
        }
    }

    @Override
    public void init() {
        this.failedLoginAttemptsByIp = new ConcurrentHashMap<>();
    }

    @Override
    public int getAttemptCacheSize() {
        return this.failedLoginAttemptsByIp.size();
    }

    private void removeOldAttempts(LogEntry logEntry) {

        LocalDateTime lastAttempt = logEntry.getDateTime();

        failedLoginAttemptsByIp.entrySet().stream().forEach(attemptByIp -> {
            if (attemptByIp.getValue().firstAttemptIsOlderThan(lastAttempt, timeWindowInMinutes)) {
                failedLoginAttemptsByIp.remove(attemptByIp.getKey());
            }
        });

    }

    private FailedLoginAttempts getFailedLoginAttemptsForIp(LogEntry logEntry) {

        FailedLoginAttempts failedLoginAttempts = failedLoginAttemptsByIp.get(logEntry.getIpAddress());
        if (failedLoginAttempts == null) {
            failedLoginAttempts = new FailedLoginAttempts(logEntry.getDateTime());
            failedLoginAttemptsByIp.put(logEntry.getIpAddress(), failedLoginAttempts);
        }

        return failedLoginAttempts;
    }

}
