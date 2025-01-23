package com.nyanmyohtet.springbootstarter.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * A custom user representation.
 */
@Getter
public class CustomUser {

    private final long id;

    private final String username;

    @JsonIgnore
    @ToString.Exclude
    private final String password;

    private final boolean enabled;

    @JsonCreator
    public CustomUser(@JsonProperty("id") long id, @JsonProperty("email") String username,
                      @JsonProperty("password") String password, boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }
}
