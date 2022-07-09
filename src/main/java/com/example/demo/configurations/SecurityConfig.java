package com.example.demo.configurations;

import com.example.demo.services.database.DatabaseUserDetailsService;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/customers/**").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/users/**").hasAnyAuthority("ADMIN")
                .and()
                .httpBasic();
        http
                .headers()
                .contentSecurityPolicy("script-src 'self'");
        return http.build();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(DatabaseUserDetailsService databaseUserDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(databaseUserDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
