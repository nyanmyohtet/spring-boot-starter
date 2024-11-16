package com.nyanmyohtet.springbootstarter.util;

import com.nyanmyohtet.springbootstarter.service.impl.CommonValidationService;

import java.time.Duration;

public final class TimeUtil {

    private TimeUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Convert the time value to Duration based on the given unit
     *
     * @param time the time value to convert
     * @param unit the time units (SECONDS, MINUTES, HOURS, ...)
     * @return the time value in millisecond
     * @throws IllegalArgumentException if the unit is not supported
     */
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

    /**
     * Convert time value to millisecond based on given unit
     * @param time the time value to convert
     * @param unit the time units (SECONDS, MINUTES, HOURS, ...)
     * @return the time value in millisecond
     * @throws IllegalArgumentException if the unit is not supported
     */
    public static long getMillisecond(int time, String unit) {
        return getDuration(time, unit).toMillis();
    }
}
