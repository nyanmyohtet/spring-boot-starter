package com.nyanmyohtet.springbootstarter.api.rest.auth.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TokenRefreshRequest {
    @NotBlank(message = "Refresh Token is mandatory")
    private String refreshToken;
}
