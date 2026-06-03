package com.josan.study_buddy.User;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    USER,
    ADMIN;

    public SimpleGrantedAuthority toGrantedAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + this.name());
    }
}