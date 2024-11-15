package com.nyanmyohtet.springbootstarter.util;

import java.time.Duration;

public class TimeUtil {
    public static Duration getDuration(int time, String unit) {
        return switch (unit.toUpperCase()) {
            case "HOURS" -> Duration.ofHours(time);
            case "MINUTES" -> Duration.ofMinutes(time);
            case "SECONDS" -> Duration.ofSeconds(time);
            default -> throw new IllegalArgumentException("Unsupported time unit: " + unit);
        };
    }
}
