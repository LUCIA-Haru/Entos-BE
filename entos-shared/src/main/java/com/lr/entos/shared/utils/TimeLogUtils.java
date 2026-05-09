package com.lr.entos.shared.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeLogUtils {
    private static LocalDateTime startTime;
    private static LocalDateTime endTime;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void start(){
        startTime = LocalDateTime.now();
    }

    public static void stop(){
        if (startTime == null) {
            throw new IllegalStateException("Timer has not been started!");
        }
        endTime = LocalDateTime.now();
    }

    public static String getDuration() {
        if (startTime == null)
            return "Timer has not started yet";

        LocalDateTime end = endTime != null ? endTime : LocalDateTime.now();
        Duration duration = Duration.between(startTime, end);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        long millis = duration.toMillis() % 1000;

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }
}
