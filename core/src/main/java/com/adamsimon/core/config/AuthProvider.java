package com.adamsimon.core.config;

import com.adamsimon.commons.dto.ErrorResponse;
import com.adamsimon.commons.dto.builders.ErrorBuilder;
import com.adamsimon.core.domain.Users;
import com.adamsimon.core.interfaces.DatabaseHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

import static com.adamsimon.commons.constants.Constants.*;


public class AuthProvider implements AuthenticationProvider {

    @Autowired
    DatabaseHandlerService databaseHandlerService;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        final AuthenticationToken tokenContainer = (AuthenticationToken) auth;
        final String token = tokenContainer.getToken();

        if (StringUtils.isEmpty(token)) {
            System.out.println("Authprovideres nouser");
            ErrorResponse errorResponse = (new ErrorBuilder.ErrorResponseBuilder())
                    .withCode(NO_USER_TOKEN_CODE)
                    .withText(NO_USER_TOKEN_STR)
                    .build();
            throw new BadCredentialsException(errorResponse.toString());
        }

        final Users storedUser = this.databaseHandlerService.getUserFromAuthToken(tokenContainer.getToken());
        //do i know this token?

        if (storedUser == null || !storedUser.getToken().get(0).equals(token)) {
            ErrorResponse errorResponse = (new ErrorBuilder.ErrorResponseBuilder())
                    .withCode(INVALID_TOKEN_CODE)
                    .withText(INVALID_USER_TOKEN_STR)
                    .build();
            throw new BadCredentialsException(errorResponse.toString());
        }

        return new AuthenticationToken(token, storedUser);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //this class is only responsible for AuthTokenContainers
        return AuthenticationToken.class.isAssignableFrom(authentication);
    }
}
