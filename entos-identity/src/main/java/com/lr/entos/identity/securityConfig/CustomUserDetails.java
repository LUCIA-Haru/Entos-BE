package com.lr.entos.identity.securityConfig;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lr.entos.identity.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record CustomUserDetails (

        UUID guid,
        String email,
        @JsonIgnore String password,
        Collection<? extends GrantedAuthority> authorities
) implements UserDetails {
    public static CustomUserDetails build(User user){
        var authorities = List.of(new SimpleGrantedAuthority(user.getRole().getName()));
        return new CustomUserDetails(user.getGuid(), user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return email;
    }
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
        return true;
    }
}
