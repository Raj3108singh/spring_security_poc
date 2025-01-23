package com.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/account", "/balance", "/cards", "/profile").authenticated()
                .requestMatchers("/notices", "/contact", "/error").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        // if we don't provide an encoding mechanism , spring security will try to
        //store the password as plain text which is not recommended and will throw error when
        //trying to access protected routes.

        // We can provide {noop} to tell that we don't need encoding like "password("{noop}admin")"

        // bcrypt hash for 12345 is $2a$12$7h7zBuMswoKZ0zOjG10SHO0CLo3e93KWNrdqoGiB0wk4rxf29EWyu
        UserDetails user = User.withUsername("user").password("{bcrypt}$2a$12$7h7zBuMswoKZ0zOjG10SHO0CLo3e93KWNrdqoGiB0wk4rxf29EWyu").authorities("user").build();
        UserDetails admin = User.withUsername("admin").password("{noop}12345").authorities("admin").build();
        return new InMemoryUserDetailsManager(user,admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //By default Bcrypt encoder will be selected if we don't mention anything in
        //{example} format. Also when using in memory it is good idea to encrypt the password text
        //as plain text password in source code can be compromised ex 12345 pwd is not recommended instead use the
        // hashed value of 12345.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    // Below code help us to check if the password being used by users are compromised on not.
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
    /**
     *
     *
     * */
}

