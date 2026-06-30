package com.pms.security.configuration;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.pms.auditlog.filter.TraceIdFilter;
import com.pms.security.dto.CustomPasswordEncoder;
import com.pms.security.service.CustomUserDetailsService;
import com.pms.security.util.filter.JwtAuthFilter;

import jakarta.servlet.http.HttpServletResponse;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final TraceIdFilter traceIdFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, CustomUserDetailsService customUserDetailsService, TraceIdFilter traceIdFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.customUserDetailsService = customUserDetailsService;
        this.traceIdFilter = traceIdFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // ✅ Disable CSRF for REST API
            .csrf(csrf -> csrf.disable())

            // ✅ Apply CORS config
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // ✅ Stateless session (JWT based)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth
                // ✅ Allow all OPTIONS preflight requests (CRITICAL for React)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ✅ Public endpoints (same as before)
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/reports/**").permitAll()
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()

                // ✅ Protected endpoints (same as before)
                .requestMatchers("/admin/**").authenticated()
                .anyRequest().authenticated()
            )

            .addFilterBefore(traceIdFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(jwtAuthFilter, TraceIdFilter.class);
        
        http.exceptionHandling(ex -> ex.accessDeniedHandler((request, response, exception) -> {
            logger.error("ACCESS DENIED");
            logger.error("URI: {}", request.getRequestURI());
            logger.error("Message: {}", exception.getMessage(), exception);

            response.sendError(HttpServletResponse.SC_FORBIDDEN, exception.getMessage());
        }));
        	

        return http.build();
    }

    // ✅ CORS Configuration — THIS WAS MISSING
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ Allowed frontend origins
        configuration.setAllowedOrigins(Arrays.asList(
            "https://pms-management-project.onrender.com",  // Production frontend
            "http://localhost:3000",                         // React dev server
            "http://localhost:5173"                          // Vite dev server
        ));

        // ✅ Allowed HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // ✅ Allowed headers
//        configuration.setAllowedHeaders(Arrays.asList(
//            "Authorization",
//            "Content-Type",
//            "X-Requested-With",
//            "Accept",
//            "Origin",
//            "Access-Control-Request-Method",
//            "Access-Control-Request-Headers"
//        ));
        
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // ✅ Expose Authorization header to React
//        configuration.setExposedHeaders(Arrays.asList(
//            "Authorization",
//            "Access-Control-Allow-Origin",
//            "Access-Control-Allow-Credentials"
//        ));
        
        configuration.setExposedHeaders(Arrays.asList("*"));

        // ✅ Allow cookies/credentials
        configuration.setAllowCredentials(true);

        // ✅ Cache preflight response for 1 hour
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            logger.error("AccessDeniedException for URI: {}", request.getRequestURI(), ex);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
        };
    }
    
    
}