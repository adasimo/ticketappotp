package com.adamsimon.appl.config;


import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan("com.adamsimon.core")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

//        .and().exceptionHandling().accessDeniedHandler(restAccessDeniedHandler);
        httpSecurity
                .addFilterBefore(createCustomFilter(), AnonymousAuthenticationFilter.class)
                .authorizeRequests().antMatchers("/h2-console/**").permitAll()
                .and()
                .csrf().disable();
        httpSecurity.headers().frameOptions().disable();
//        httpSecurity
//                .csrf().disable();
//                .ignoringAntMatchers("/**").and().authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/**").permitAll()//.hasAnyRole("ADMIN", "USER")
//                .antMatchers(HttpMethod.PUT, "/**").permitAll()//.hasAnyRole("ADMIN", "USER")
//                .antMatchers(HttpMethod.DELETE, "/**").permitAll()//.hasAnyRole("ADMIN", "USER")
//                .and().sessionManagement().sessionFixation().migrateSession()
//                .and().requestCache().requestCache(new NullRequestCache()).and()
//                .cors().and()
//                .headers().frameOptions().disable()
//                .and().formLogin().permitAll();

    }

//    @Bean
//    protected AuthenticationEntryPoint clientAuthorizationEntryPoint() {
//        System.out.println("1111111111111111111");
//        return new RestAuthenticationEntryPoint();
//    }

    //Note, we don't register this as a bean as we don't want it to be added to the main Filter chain, just the spring security filter chain
    protected AbstractAuthenticationProcessingFilter createCustomFilter() throws Exception {
        //here we define the interfaces which don't need any authorisation
        AuthFilter filter = new AuthFilter(new NegatedRequestMatcher(
                new AndRequestMatcher(
                        new AntPathRequestMatcher("/login"),
                        new AntPathRequestMatcher("/health")
                )
        ));
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }
}
