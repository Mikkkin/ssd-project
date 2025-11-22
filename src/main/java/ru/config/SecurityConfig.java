package ru.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import lombok.RequiredArgsConstructor;
import ru.service.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        var del = new CsrfTokenRequestAttributeHandler();

        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                
                .requestMatchers("/api/candidates/**").hasAnyRole("CANDIDATE", "ADMIN")
                
                .requestMatchers("/api/interviews/**").hasAnyRole("HR", "ADMIN")
                
                .requestMatchers("/api/vacancies/**").hasAnyRole("HR", "ADMIN")
                
                .requestMatchers("/api/offers/**").hasAnyRole("HR", "ADMIN")

                .requestMatchers("/api/users/**").hasRole("ADMIN")

                .requestMatchers("/api/auth/{userId}/**").hasRole("ADMIN")
                
                .requestMatchers("/api/applications/**").hasAnyRole("HR", "ADMIN", "CANDIDATE")
                
                .anyRequest().authenticated()
            )
            
            .httpBasic(basic -> basic
                .realmName("Basic authentication")
            )
            
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(del::handle)
                .ignoringRequestMatchers("/api/auth/register", "/api/auth/login")
            )
            
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation(sessionFixation -> sessionFixation.migrateSession())
            )
            
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(401);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"error\": \"Authentication required\"}");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(403);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"error\": \"Access denied\"}");
                })
            );

        return http.build();
    }
}
