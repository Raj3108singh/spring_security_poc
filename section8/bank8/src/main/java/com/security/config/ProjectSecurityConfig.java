package com.security.config;

import com.security.exceptionHandling.CustomAccessDeniedHandler;
import com.security.exceptionHandling.CustomBasicAuthenticationEntryPoint;
import com.security.filters.CustomCsrfFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("!prod")

public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        http.cors(corsConfig -> corsConfig.configurationSource(
                        new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration corsConfiguration = new CorsConfiguration();
                                corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                                corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                                corsConfiguration.setAllowCredentials(true);
                                corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                                corsConfiguration.setMaxAge(3600L);
                                return corsConfiguration;
                            }
                        }
                ))
                .securityContext(secConfig -> secConfig.requireExplicitSave(false))
                .sessionManagement((scm) -> scm.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .sessionManagement((smc) -> smc.sessionFixation(sfc -> sfc.newSession())
                        .invalidSessionUrl("/invalidSession").maximumSessions(1).maxSessionsPreventsLogin(true))
                .requiresChannel((rcc) -> rcc.anyRequest().requiresInsecure())
                .csrf((csrfConfig) -> csrfConfig
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("/contact")
                )
                .addFilterAfter(new CustomCsrfFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/account", "/balance", "/cards", "/profile").authenticated()
                        .requestMatchers("/notices", "/contact", "/error", "/register", "/invalidSession").permitAll()
                );

        http.formLogin(withDefaults());
        http.httpBasic((cba) -> cba.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling((ehc) -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

}

