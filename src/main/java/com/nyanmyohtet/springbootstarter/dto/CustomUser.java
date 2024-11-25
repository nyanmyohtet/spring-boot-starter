package com.nyanmyohtet.springbootstarter.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * A custom user representation.
 */
@Getter
public class CustomUser {

    private final long id;

    private final String username;

    @JsonIgnore
    private final String password;

    @JsonCreator
    public CustomUser(@JsonProperty("id") long id, @JsonProperty("email") String username,
                      @JsonProperty("password") String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
