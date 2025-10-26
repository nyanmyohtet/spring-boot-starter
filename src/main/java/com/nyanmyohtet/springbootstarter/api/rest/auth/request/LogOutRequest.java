package com.nyanmyohtet.springbootstarter.api.rest.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogOutRequest {
    @NotBlank(message = "Refresh Token is mandatory")
    private String refreshToken;
}
