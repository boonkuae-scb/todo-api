package com.bk.todos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthResponse {
    private String scope;

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "token_type")
    private String tokenType;

    @JsonProperty(value = "expires_in")
    private Integer expiresIn;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;
}