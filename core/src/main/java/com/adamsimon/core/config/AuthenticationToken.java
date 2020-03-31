package com.adamsimon.core.config;

import com.adamsimon.commons.dto.UserRoles;
import com.adamsimon.core.domain.Users;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuthenticationToken extends AbstractAuthenticationToken {
    private final String token;
    private final Users user;

    public AuthenticationToken(String token) {
        super(null);
        this.token = token;
        this.user = null;
        setAuthenticated(false);
    }

    public AuthenticationToken(String token, Users user) {
        //note that the constructor needs a collection of GrantedAuthority
        //but our User have a collection of our UserRole's
        super(new ArrayList<UserRoles>(Collections.singleton(UserRoles.USER)));
        this.token = token;
        this.user = user;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return getToken();
    }

    @Override
    public Object getPrincipal() {
        return getUser();
    }

    public String getToken() {
        return token;
    }

    public Users getUser() {
        return user;
    }
}
