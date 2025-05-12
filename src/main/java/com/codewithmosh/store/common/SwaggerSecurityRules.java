package com.codewithmosh.store.common;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class SwaggerSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry
                // HTML entrypoint
                .requestMatchers("/swagger-ui.html").permitAll()
                // the index (served under /swagger-ui/index.html)
                .requestMatchers("/swagger-ui/index.html").permitAll()
                // static assets (CSS, JS, etc)
                .requestMatchers("/swagger-ui/**").permitAll()
                // OpenAPI JSON
                .requestMatchers("/v3/api-docs/**").permitAll();
    }
}

