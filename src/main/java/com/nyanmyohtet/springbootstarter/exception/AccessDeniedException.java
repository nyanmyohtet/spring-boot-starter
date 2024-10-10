package com.nyanmyohtet.springbootstarter.exception;

public class AccessDeniedException extends RuntimeException {
    // private static final long serialVersionUID = 982472988463223471L;
    public AccessDeniedException(String message) {
        super(message);
    }
}
