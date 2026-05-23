package com.lr.entos.identity.securityConfig;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //for @PreAuthorize
@Slf4j
@ConfigurationPropertiesScan("com.lr.entos.identity.securityConfig.properties")
public class SecurityConfig {
    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver resolver;//for accessdeniedexception to be professional so i redirect to globalexception to inject

    private final CustomUserDetailsService userDetailsService;
    private final OAuth2AuthenticationSuccessHandler oauth2SuccessHandler;
    private final AuthTokenFilter authenticationJwtTokenFilter;


    public SecurityConfig(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver, CustomUserDetailsService userDetailsService,
                          @org.springframework.context.annotation.Lazy OAuth2AuthenticationSuccessHandler oauth2SuccessHandler,
                          AuthTokenFilter authenticationJwtTokenFilter) {
        this.resolver = resolver;
        this.userDetailsService = userDetailsService;
        this.oauth2SuccessHandler = oauth2SuccessHandler;
        this.authenticationJwtTokenFilter = authenticationJwtTokenFilter;

    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/v1/auth/**").permitAll() // Local Signup/Login
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/login/**", "/oauth2/**").permitAll() // OAuth2 internal routes
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-ui/**",
                                "/swagger-ui/index.html",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                resolver.resolveException(request,response,null,authException))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                resolver.resolveException(request, response, null, accessDeniedException))
                )
                .authenticationProvider(authenticationProvider())
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oauth2SuccessHandler)
                )
                .addFilterBefore(authenticationJwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
