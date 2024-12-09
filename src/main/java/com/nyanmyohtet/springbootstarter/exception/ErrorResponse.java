package com.nyanmyohtet.springbootstarter.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ErrorResponse {
    private Instant timestamp; // Adds context, helpful for debugging.
    private Integer status;    // HTTP status code as an integer (e.g., 400).
    private String error;      // Human-readable HTTP status description.
    private String code;       // Custom error code to facilitate internal error mapping or logging
    private String message;    // Explains the problem in simple terms for clients.
    private String path;       // Indicates the endpoint where the error occurred.
}
