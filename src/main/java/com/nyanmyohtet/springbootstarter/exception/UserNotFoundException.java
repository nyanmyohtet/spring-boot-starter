package com.nyanmyohtet.springbootstarter.exception;

public class UserNotFoundException extends RuntimeException {
    //private static final long serialVersionUID = 982472985983223471L;
    public UserNotFoundException(String message) {
        super(message);
    }
}
