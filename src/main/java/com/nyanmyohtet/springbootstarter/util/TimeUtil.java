package com.nyanmyohtet.springbootstarter.util;

import com.nyanmyohtet.springbootstarter.service.impl.CommonValidationService;

import java.time.Duration;

public final class TimeUtil {

    private TimeUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Duration getDuration(int time, String unit) {
        CommonValidationService.validatePositive(time, "Time");
        CommonValidationService.validateString(unit, "Unit");

        return switch (unit.toUpperCase()) {
            case "HOURS" -> Duration.ofHours(time);
            case "MINUTES" -> Duration.ofMinutes(time);
            case "SECONDS" -> Duration.ofSeconds(time);
            default -> throw new IllegalArgumentException("Unsupported time unit: " + unit);
        };
    }

    public static long getMillisecond(int time, String unit) {
        CommonValidationService.validatePositive(time, "Time");
        CommonValidationService.validateString(unit, "Unit");

        return switch (unit.toUpperCase()) {
            case "HOURS" -> Duration.ofHours(time).toMillis();
            case "MINUTES" -> Duration.ofMinutes(time).toMillis();
            case "SECONDS" -> Duration.ofSeconds(time).toMillis();
            default -> throw new IllegalArgumentException("Unsupported time unit: " + unit);
        };
    }
}
