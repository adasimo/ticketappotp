package com.adamsimon.appl.config;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.builders.ReservationBuilder;
import com.adamsimon.core.domain.User;
import com.adamsimon.core.interfaces.DatabaseHandlerService;
import com.adamsimon.partner.domain.TicketModuleUser;
import com.adamsimon.partner.interfaces.PartnerDatabaseCallerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

import static com.adamsimon.commons.constants.Constants.*;


public class AuthProvider implements AuthenticationProvider {

    @Autowired
    private DatabaseHandlerService coreDatabaseHandlerService;
    @Autowired
    private PartnerDatabaseCallerService partnerDatabaseCallerService;
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

        if (path.contains("api")) {
            logger.info("Api path found, checking token");
            final User storedUser = this.coreDatabaseHandlerService.getUserFromAuthToken(token);

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

        } else if (path.contains("partner")) {
            logger.info("Partner path found, checking token");
            final TicketModuleUser ticketModuleUser = this.partnerDatabaseCallerService.getUserByToken(token);

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
            return new AuthenticationTokenApi(token, path);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationTokenApi.class.isAssignableFrom(authentication);
    }
}
