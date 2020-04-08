package com.adamsimon.partner.testconfig;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.builders.ReservationBuilder;
import com.adamsimon.partner.domain.TicketModuleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

import static com.adamsimon.commons.constants.Constants.*;


public class AuthProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(AuthProvider.class);

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        final AuthenticationTokenPartner tokenContainer = (AuthenticationTokenPartner) auth;
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
        } else if (path.contains("partner")) {
            if (StringUtils.isEmpty(token)) {
                AbstractPartnerResponse errorResponse = (new ReservationBuilder.ReservationResponseBuilder())
                        .getFailedBuilder()
                        .withErrorCodeToFail(NO_PARTNER_TOKEN_CODE)
                        .withErrorMessageToFail(NO_PARTNER_TOKEN_STR)
                        .build();
                logger.info(errorResponse.toString());
                throw new BadCredentialsException(errorResponse.toString());
            }
            logger.info("User-Token is provided for PARTNER");
        }

        if (path.contains("partner")) {
            logger.info("Partner path found, checking token");
            final TicketModuleUser ticketModuleUser = new TicketModuleUser();
            ticketModuleUser.setId(999L);
            ticketModuleUser.setToken("a5eA4E8F7fgu5hk6af3fsaGgfAteAe46FL3aI67EfN");

            if (ticketModuleUser == null || !ticketModuleUser.getToken().equals(token)) {
                AbstractPartnerResponse errorResponse = (new ReservationBuilder.ReservationResponseBuilder())
                        .getFailedBuilder()
                        .withErrorCodeToFail(INVALID_PARTNERTOKEN_CODE)
                        .withErrorMessageToFail(INVALID_PARTNER_TOKEN_STR)
                        .build();
                logger.info(errorResponse.toString());
                throw new BadCredentialsException(errorResponse.toString());
            }
            logger.info("Token is valid");
            return new AuthenticationTokenPartner(token, path, ticketModuleUser);
        } else {
            return new AuthenticationTokenPartner(token, path);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationTokenPartner.class.isAssignableFrom(authentication);
    }
}
