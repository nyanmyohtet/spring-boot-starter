package com.nyanmyohtet.springbootstarter.exception;

public class BadRequestException extends RuntimeException {
    //private static final long serialVersionUID = 982472985983223471L;
    public BadRequestException(String message) {
        super(message);
    }
}
