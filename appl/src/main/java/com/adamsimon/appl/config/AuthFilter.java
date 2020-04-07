package com.adamsimon.appl.config;

import static com.adamsimon.commons.constants.Constants.*;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.builders.ReservationBuilder;
import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AuthFilter extends AbstractAuthenticationProcessingFilter {

    private final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        ((HttpServletResponse)response).setContentType("application/json; charset=UTF-8");
        logger.info("AuthFilter filtering by token");

        this.setAuthenticationSuccessHandler((request1, response1, authentication) -> {
            chain.doFilter(request1, response1);
        });
        super.doFilter(request, response, chain);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            final String path = request.getRequestURI();
            final String tokenValue = getTokenValue(request, path);
            logger.info("Attempt Authentication for " + path);

            AuthenticationTokenApi token = new AuthenticationTokenApi(tokenValue, path);
            token.setDetails(authenticationDetailsSource.buildDetails(request));

            return this.getAuthenticationManager().authenticate(token);
        } catch (AuthenticationException aue) {
            handleAuthExc(request.getRequestURL().toString(), response, aue);
        }
        return null;
    }

    private String getTokenValue(HttpServletRequest req, final String pathuri) {
        return Collections.list(req.getHeaderNames()).stream()
                .filter(header -> {
                    if (pathuri.contains("api")) {
                        return header.equalsIgnoreCase(TOKEN_HEADER);
                    } else {
                        return header.equalsIgnoreCase(TOKEN_HEADER_PARTNER);
                    }
                })
                .map(req::getHeader)
                .findFirst()
                .orElse(null);
    }

    private AbstractPartnerResponse mapAppErrorToErrorObj(final String message) {
        final JsonObject jsonObject = new Gson().fromJson(message, JsonObject.class);
        logger.info("mapAppErrorToErrorObj: " + jsonObject.toString());
        return new ReservationBuilder.ReservationResponseBuilder().getFailedBuilder().withErrorCodeToFail(jsonObject.get("code").getAsInt()).withErrorMessageToFail(jsonObject.get("text").getAsString()).build();
    }

    private void handleAuthExc(final String url, HttpServletResponse response, final AuthenticationException aue) throws IOException {
        logger.info("Got AuthenticationException: " + aue.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        final Map<String, Object> errorDetails = createErrorMapping(url, aue);
        objectMapper.writeValue(response.getWriter(), errorDetails);
    }

    private Map<String, Object> createErrorMapping(final String url, final AuthenticationException aue) {
        final ReservationFailedResponse failedResponse = (ReservationFailedResponse) mapAppErrorToErrorObj(aue.getMessage());

        final Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("success", failedResponse.getSuccess());
        errorDetails.put("errorMessage", failedResponse.getErrorMessage());
        errorDetails.put("errorCode", failedResponse.getErrorCode());

        final Map<String, Object> linksMap = new HashMap<>();
        final Map<String, Object> selfMap = new HashMap<>();
        selfMap.put("href", url);
        linksMap.put("self", selfMap);
        errorDetails.put("_links", linksMap);
        logger.info("Errors Mapped: " + errorDetails.toString());
        return errorDetails;
    }
}
