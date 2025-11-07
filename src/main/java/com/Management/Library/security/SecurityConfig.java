package com.Management.Library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * This class configures Spring Security to handle the "login" requirement.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Creates a simple in-memory user for testing.
     * In a "real" app, this would come from the database.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER") // A user with the role "USER"
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin123")
                .roles("ADMIN", "USER") // An admin with two roles
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    /**
     * Configures the security rules for our application.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Allow anyone to access the H2 console
                        .requestMatchers("/h2-console/**").permitAll()
                        // Any other request (e.g., to /api/...) must be authenticated
                        .anyRequest().authenticated()
                )
                // Use HTTP Basic authentication (the popup window in the browser)
                .httpBasic(withDefaults())
                // Use a simple form login
                .formLogin(withDefaults());

        // --- This is required for H2 console to work with security ---
        // Do not do this in production!
        http.csrf(csrf -> csrf.disable());
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        // --- End H2 console config ---

        return http.build();
    }
}
