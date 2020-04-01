package com.adamsimon.core.config;

import static com.adamsimon.commons.constants.Constants.*;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.builders.ReservationBuilder;
import com.adamsimon.commons.dto.responseDto.ErrorResponse;
import com.adamsimon.commons.dto.builders.ErrorBuilder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class AuthFilter extends AbstractAuthenticationProcessingFilter {

    public AuthFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        try {
            final String token = getTokenValue((HttpServletRequest) request);
            final String path = ((HttpServletRequest) request).getRequestURI();

//            if (path.contains("partner")) {
//                System.out.println("partnercont");
//                super.doFilter(request, response, chain);
//                return;
//            }
            System.out.println("runs after partner");
            //On success keep going on the chain
            this.setAuthenticationSuccessHandler((request1, response1, authentication) -> {
                chain.doFilter(request1, response1);
            });

            super.doFilter(request, response, chain);

        } catch (AuthenticationException aue) {
            ((HttpServletResponse)response).setContentType("application/json");
            ((HttpServletResponse)response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ((HttpServletResponse)response).getOutputStream().println(aue.getMessage());
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            System.out.println("attemptauth");
            final String tokenValue = getTokenValue(request);
            final String path = request.getRequestURI();

//            if (path.contains("api") || path.contains("partner")) {
//                if (StringUtils.isEmpty(tokenValue)) {
//                    AbstractPartnerResponse errorResponse = (new ReservationBuilder.ReservationResponseBuilder())
//                            .getFailedBuilder()
//                            .withErrorCodeToFail(NO_USER_TOKEN_CODE)
//                            .withErrorMessageToFail(NO_USER_TOKEN_STR)
//                            .build();
//                    throw new BadCredentialsException(errorResponse.toString());
//                }
//            }

            AuthenticationToken token = new AuthenticationToken(tokenValue, path);
            token.setDetails(authenticationDetailsSource.buildDetails(request));

            return this.getAuthenticationManager().authenticate(token);
        } catch (AuthenticationException aue) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().println(aue.getMessage());
        }
        return null;
    }

    private String getTokenValue(HttpServletRequest req) {
        //find the header which contains our token (ignore the header-name case)
        return Collections.list(req.getHeaderNames()).stream()
                .filter(header -> header.equalsIgnoreCase(TOKEN_HEADER))
                .map(req::getHeader)
                .findFirst()
                .orElse(null);
    }
}
