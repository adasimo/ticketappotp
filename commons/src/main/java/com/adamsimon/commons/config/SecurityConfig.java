package com.adamsimon.commons.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.savedrequest.NullRequestCache;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().ignoringAntMatchers("/**").and().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/**").permitAll()//.hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.PUT, "/**").permitAll()//.hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE, "/**").permitAll()//.hasAnyRole("ADMIN", "USER")
                .and().sessionManagement().sessionFixation().migrateSession()
                .and().requestCache().requestCache(new NullRequestCache()).and()
                .cors().and()
                .headers().frameOptions().disable()
                .and().formLogin().permitAll();

    }
}
