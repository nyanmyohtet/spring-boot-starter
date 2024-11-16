package com.nyanmyohtet.springbootstarter.service.impl;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class CommonValidationService {

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

        TimeUnit[] timeUnitValues = TimeUnit.values();
        try {
            TimeUnit.valueOf(value);
        } catch (Exception e) {
            throw new IllegalArgumentException(fieldName + " is not valid. Given " + value + ". Supported values are "
                    + Arrays.toString(timeUnitValues));
        }
    }

    public static void validateIpAddress(String ipAddress) {
        String fieldName = "IP Address";
        validateString(ipAddress, fieldName);

        if (!ipAddress.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$")) {
            throw new IllegalArgumentException("Invalid " + fieldName + " format.");
        }
    }
}
