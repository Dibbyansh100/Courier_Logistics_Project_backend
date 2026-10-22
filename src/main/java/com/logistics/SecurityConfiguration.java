package com.logistics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity // disable default security and enables customized configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain SFC(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable()) // protects browser based applications from malicious put/post
            // CSRF- Cross Site Request Forgery
            // CSRF is disabled due to stateless nature of REST API
            .authorizeHttpRequests(auth -> auth // Defines Permission to access a resources (Authorization)
                // Public endpoints: Auth login, User registration, Customer signup
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/users/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/customers", "/api/customers/**").permitAll()

                // Role-based permissions:
                // Only ADMIN can manage Warehouses and Delivery Agents
                .requestMatchers("/api/warehouses/**").hasRole("ADMIN")
                .requestMatchers("/api/agents/**").hasRole("ADMIN")
                // CUSTOMER & ADMIN can manage/view Customers, Payments, Packages
                .requestMatchers("/api/customers/**").hasAnyRole("ADMIN", "CUSTOMER")
                .requestMatchers("/api/payments/**").hasAnyRole("ADMIN", "CUSTOMER")
                .requestMatchers("/api/packages/**").hasAnyRole("ADMIN", "CUSTOMER")
                // ADMIN, DELIVERY & CUSTOMER can view/update Shipments & Tracking
                .requestMatchers("/api/shipments/**").hasAnyRole("ADMIN", "DELIVERY", "CUSTOMER")
                .requestMatchers("/api/tracking/**").hasAnyRole("ADMIN", "DELIVERY", "CUSTOMER")
                // Authenticates every request
                .anyRequest().authenticated() // verifies user credentials for identity
            )
            .httpBasic(Customizer.withDefaults()); // removes form login and enable http basic login
        return http.build(); // return bean of SFC
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    // Configure DaoAuthenticationProvider as Ap to perform DB authentication
    public DaoAuthenticationProvider DaoAuthenticationProvider() {
        // pass the autowired reference variable of UDS to fetch user data from DB
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(userDetailsService);
        dao.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        // set PasswordEncoder to complete and encrypt password using impl
        // class NoOpPasswordEncoder
        return dao;
    }
}
