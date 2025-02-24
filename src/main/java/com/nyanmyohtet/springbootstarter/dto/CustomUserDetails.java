package com.nyanmyohtet.springbootstarter.dto;

import com.nyanmyohtet.springbootstarter.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public final class CustomUserDetails extends CustomUser implements UserDetails {

    public CustomUserDetails(User user) {
        super(user.getId(), user.getUsername(), user.getPasswordHash(), user.isEnabled(), user.getAuthorities());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

//    @Override
//    public String getUsername() {
//        return getEmail();
//    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }
}
