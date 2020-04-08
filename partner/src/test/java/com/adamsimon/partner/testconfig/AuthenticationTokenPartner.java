package com.adamsimon.partner.testconfig;

import com.adamsimon.commons.dto.securityDto.UserRoles;
import com.adamsimon.partner.domain.TicketModuleUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.ArrayList;
import java.util.Collections;

public class AuthenticationTokenPartner extends AbstractAuthenticationToken {
    private final String token;
    private final TicketModuleUser user;
    private final String path;

    public AuthenticationTokenPartner(String token, String path) {
        super(null);
        this.token = token;
        this.path = path;
        this.user = null;
        setAuthenticated(false);
    }

    public AuthenticationTokenPartner(String token, String path, TicketModuleUser user) {
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

    public TicketModuleUser getUser() {
        return user;
    }

    public String getPath() { return path; }
}
