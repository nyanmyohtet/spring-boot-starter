package com.nyanmyohtet.springbootstarter.api.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponse {
    private String message;
    private Integer status;
}
