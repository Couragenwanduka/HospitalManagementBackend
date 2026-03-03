package com.hospitalManagement.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for testing
            .authorizeHttpRequests(authz -> authz
                .anyRequest().authenticated()) // All requests require authentication
            .httpBasic(httpBasic -> {}); // Use basic auth
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("admin")
                               .password("{noop}password") // {noop} means no password encoding
                               .roles("USER")
                               .build();
        return new InMemoryUserDetailsManager(user);
    }
}