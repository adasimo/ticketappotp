package com.adamsimon.partner.testconfig;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .addFilterBefore(createCustomFilter(), AnonymousAuthenticationFilter.class)
                .csrf().disable();
        // a h2-consolehoz kell
        httpSecurity.headers().frameOptions().disable();
    }

    protected AbstractAuthenticationProcessingFilter createCustomFilter() throws Exception {
        AuthFilter filter = new AuthFilter(new NegatedRequestMatcher(
                new AndRequestMatcher(
                        new AntPathRequestMatcher("/login"),
                        new AntPathRequestMatcher("/health"),
                        new AntPathRequestMatcher("/h2-console/**")
                )
        ));
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }
}
