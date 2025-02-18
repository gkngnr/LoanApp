package com.example.demo.config;

import com.example.demo.security.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/customers", "/api/v1/loans")
                            .hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/loans/pay")
                            .hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/loans/**")
                            .hasAnyRole(Role.ADMIN.name(), Role.CUSTOMER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/installments/**")
                            .hasAnyRole(Role.ADMIN.name(), Role.CUSTOMER.name())
                        .requestMatchers(
                                "/v3/api-docs/**",  // OpenAPI docs
                                "/swagger-ui/**",   // Swagger UI static files
                                "/swagger-ui.html", // Main Swagger page
                                "/webjars/**"       // WebJars used by Swagger
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationManager(authenticationManager)
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userService) {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        var providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);

        return providerManager;
    }

}
