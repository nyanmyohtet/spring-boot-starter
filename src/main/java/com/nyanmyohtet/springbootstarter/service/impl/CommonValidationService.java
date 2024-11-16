package com.nyanmyohtet.springbootstarter.service.impl;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public final class CommonValidationService {

    private CommonValidationService() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void validateString(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be empty.");
        }
    }

    public static void validatePositive(Integer value, String message) {
        if (value <= 0) throw new IllegalArgumentException(message + value);
    }

    public static void validTimeUnit(String value) {
        String fieldName = "Timeunit";
        validateString(value, fieldName);

        try {
            TimeUnit.valueOf(value);
        } catch (IllegalArgumentException e) {
            TimeUnit[] timeUnitValues = TimeUnit.values();
            throw new IllegalArgumentException(fieldName + " is not valid. Given " + value + ". Supported values are "
                    + Arrays.toString(timeUnitValues));
        }
    }

    public static void validateIpAddress(String ipAddress) {
        String fieldName = "IP Address";
        validateString(ipAddress, fieldName);

        if (!ipAddress.matches("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
            throw new IllegalArgumentException("Invalid " + fieldName + " format.");
        }
    }
}
