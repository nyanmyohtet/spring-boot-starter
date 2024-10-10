package com.nyanmyohtet.springbootstarter.exception;

public class ResourceNotFoundException extends RuntimeException {
    //private static final long serialVersionUID = 1238462578349834273L;
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
