package com.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {
    //This config will override the default config where all the routes by default are
    //protected, and we see a login page.

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/account", "/balance", "/cards", "/profile").authenticated()
                .requestMatchers("/notices", "/contact", "/error").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }
}

/*
 * Suppose we have the use case where we only need to work with APIs and there is no need for
 * form validation through username and password, in that case we can disable Form Login.
 * same goes for in case we don't need http basic login
 *      http.formLogin((flc)-> flc.disable());
 *        http.httpBasic((hbc)-> hbc.disable());
 * */