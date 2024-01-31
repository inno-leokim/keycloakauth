package com.brand13.keycloakauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.brand13.keycloakauth.handler.KeycloakLogoutHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final KeycloakLogoutHandler keycloakLogoutHandler;

    public SecurityConfig(KeycloakLogoutHandler keycloakLogoutHandler) {
        this.keycloakLogoutHandler = keycloakLogoutHandler;
    }
    
    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
            auth -> 
                auth
                    .requestMatchers(new AntPathRequestMatcher("/customers*"))
                    .hasRole("user")
                    .requestMatchers(new AntPathRequestMatcher("/"))
                    .permitAll()
                    .anyRequest()
                    .authenticated());
        
        http.oauth2ResourceServer(
            oauth2 ->
                oauth2  
                    .jwt(Customizer.withDefaults())
        );

        http.oauth2Login(Customizer.withDefaults())
            .logout(
                logoutCustomizer -> 
                    logoutCustomizer
                    .addLogoutHandler(keycloakLogoutHandler)
                    .logoutSuccessUrl("/")
        );

        return http.build();
    }

}
