package com.nyanmyohtet.springbootstarter.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nyanmyohtet.springbootstarter.enums.Role;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

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

    private final Collection<? extends GrantedAuthority> authorities;

    @JsonCreator
    public CustomUser(@JsonProperty("id") long id, @JsonProperty("email") String username,
                      @JsonProperty("password") String password, boolean enabled, Set<Role> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }
}
