package com.adamsimon.core.config;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.builders.ReservationBuilder;
import com.adamsimon.commons.dto.responseDto.ErrorResponse;
import com.adamsimon.commons.dto.builders.ErrorBuilder;
import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.adamsimon.core.domain.User;
import com.adamsimon.core.interfaces.DatabaseHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.adamsimon.commons.constants.Constants.*;


public class AuthProvider implements AuthenticationProvider {

    @Autowired
    DatabaseHandlerService databaseHandlerService;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        final AuthenticationToken tokenContainer = (AuthenticationToken) auth;
        final String token = tokenContainer.getToken();
        final String path = tokenContainer.getPath();
        System.out.println("authprovider auth");
//        if (StringUtils.isEmpty(token)) {
//            AbstractPartnerResponse errorResponse = (new ReservationBuilder.ReservationResponseBuilder())
//                    .getFailedBuilder()
//                    .withErrorCodeToFail(NO_USER_TOKEN_CODE)
//                    .withErrorMessageToFail(NO_USER_TOKEN_STR)
//                    .build();
//            throw new BadCredentialsException(errorResponse.toString());
//        }
        if (path.contains("api") || path.contains("partner")) {
            if (StringUtils.isEmpty(token)) {
                AbstractPartnerResponse errorResponse = (new ReservationBuilder.ReservationResponseBuilder())
                        .getFailedBuilder()
                        .withErrorCodeToFail(NO_USER_TOKEN_CODE)
                        .withErrorMessageToFail(NO_USER_TOKEN_STR)
                        .build();
                throw new BadCredentialsException(errorResponse.toString());
            }
        }

        System.out.println("PATH: " + path);
        if (path.contains("api")) {
            final User storedUser = this.databaseHandlerService.getUserFromAuthToken(tokenContainer.getToken());
            //do i know this token?

            if (storedUser == null || !storedUser.getToken().get(0).equals(token)) {
                AbstractPartnerResponse errorResponse = (new ReservationBuilder.ReservationResponseBuilder())
                        .getFailedBuilder()
                        .withErrorCodeToFail(INVALID_TOKEN_CODE)
                        .withErrorMessageToFail(INVALID_USER_TOKEN_STR)
                        .build();
                throw new BadCredentialsException(errorResponse.toString());
            }
            return new AuthenticationToken(token, path, storedUser);
        } else if (path.contains("partner")) {
            System.out.println("partnert ag in auth provider");
            User user = new User();
            List<String> tokens = new ArrayList<String>();
            tokens.add("TICKET");
            user.setToken(tokens);
            user.setEmail("aaaaa");
            user.setName("wwww");
            user.setUserId(5555L);
            return new AuthenticationToken(token, path, user);
        } else {
            return new AuthenticationToken(token, path);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //this class is only responsible for AuthTokenContainers
        return AuthenticationToken.class.isAssignableFrom(authentication);
    }
}
