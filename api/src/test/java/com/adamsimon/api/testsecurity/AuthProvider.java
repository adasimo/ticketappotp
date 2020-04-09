package com.adamsimon.api.testsecurity;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.builders.ReservationBuilder;
import com.adamsimon.core.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.adamsimon.commons.constants.Constants.*;


public class AuthProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(AuthProvider.class);

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        final AuthenticationTokenApi tokenContainer = (AuthenticationTokenApi) auth;
        final String token = tokenContainer.getToken();
        final String path = tokenContainer.getPath();
        logger.info("AuthProvider authenticating path: " + path);
        if (path.contains("api")) {
            if (StringUtils.isEmpty(token)) {
                AbstractPartnerResponse errorResponse = (new ReservationBuilder.ReservationResponseBuilder())
                        .getFailedBuilder()
                        .withErrorCodeToFail(NO_USER_TOKEN_CODE)
                        .withErrorMessageToFail(NO_USER_TOKEN_STR)
                        .build();
                logger.info(errorResponse.toString());
                throw new BadCredentialsException(errorResponse.toString());
            }
            logger.info("User-Token is provided for API");
        }
        if (path.contains("api")) {
            logger.info("Api path found, checking token");
            List<String> testTokens = new ArrayList<>();
            testTokens.add(token);
            final User storedUser = new User(1L, "testname", "email", testTokens);

            if (storedUser == null || !storedUser.getToken().get(0).equals(token)) {
                //csak az első tokeneket tekintem nem lejártnak
                AbstractPartnerResponse errorResponse = (new ReservationBuilder.ReservationResponseBuilder())
                        .getFailedBuilder()
                        .withErrorCodeToFail(INVALID_TOKEN_CODE)
                        .withErrorMessageToFail(INVALID_USER_TOKEN_STR)
                        .build();
                logger.info(errorResponse.toString());
                throw new BadCredentialsException(errorResponse.toString());
            }
            logger.info("Token is valid");
            return new AuthenticationTokenApi(token, path, storedUser);
        } else {
            return new AuthenticationTokenApi(token, path);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationTokenApi.class.isAssignableFrom(authentication);
    }
}
