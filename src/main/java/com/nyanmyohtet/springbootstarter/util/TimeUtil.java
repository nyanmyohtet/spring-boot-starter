package com.nyanmyohtet.springbootstarter.util;

import com.nyanmyohtet.springbootstarter.service.impl.CommonValidationService;

import java.time.Duration;

public class TimeUtil {
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
}
