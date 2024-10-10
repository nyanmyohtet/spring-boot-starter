package com.nyanmyohtet.springbootstarter.exception;

public class UnauthorizedException extends RuntimeException {
    //private static final long serialVersionUID = 395472985983223471L;
    public UnauthorizedException(String message) {
        super(message);
    }
}
