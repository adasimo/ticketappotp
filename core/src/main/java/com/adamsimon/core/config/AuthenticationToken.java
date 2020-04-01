package com.adamsimon.core.config;

import com.adamsimon.commons.dto.securityDto.UserRoles;
import com.adamsimon.core.domain.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.ArrayList;
import java.util.Collections;

public class AuthenticationToken extends AbstractAuthenticationToken {
    private final String token;
    private final User user;
    private final String path;

    public AuthenticationToken(String token, String path) {
        super(null);
        this.token = token;
        this.path = path;
        this.user = null;
        setAuthenticated(false);
    }

    public AuthenticationToken(String token, String path, User user) {
        //note that the constructor needs a collection of GrantedAuthority
        //but our User have a collection of our UserRole's
        super(new ArrayList<UserRoles>(Collections.singleton(UserRoles.USER)));
        this.token = token;
        this.user = user;
        this.path = path;
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

    public User getUser() {
        return user;
    }

    public String getPath() { return path; }
}
