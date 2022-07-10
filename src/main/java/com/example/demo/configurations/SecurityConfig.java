package com.example.demo.configurations;

import com.example.demo.repositories.UserRepository;
import com.example.demo.services.database.DatabaseUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    @Bean
    public UserDetailsService databaseUserDetailsService(
            UserRepository userRepository) {
        return new DatabaseUserDetailsService(userRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/customers/**").hasAnyAuthority(ADMIN, USER)
                .antMatchers("/users/**").hasAnyAuthority(ADMIN)
                .and()
                .httpBasic();
        // xss prevention
        http
                .headers()
                .contentSecurityPolicy("script-src 'self'");
        return http.build();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
