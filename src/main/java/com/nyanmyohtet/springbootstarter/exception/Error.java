package com.nyanmyohtet.springbootstarter.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Error {
    private String message;
    private Integer status;
}
