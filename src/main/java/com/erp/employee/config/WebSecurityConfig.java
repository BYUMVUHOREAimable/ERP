package com.erp.employee.config;

import com.erp.employee.security.CustomUserDetailsService;
import com.erp.employee.security.JwtAuthenticationEntryPoint;
import com.erp.employee.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(request ->
                        request.requestMatchers(WHITE_LIST).permitAll()
                                .requestMatchers(ADMIN_WHITE_LIST).hasRole("ADMIN")
                                .requestMatchers(ADMIN_MANAGER_WHITE_LIST).hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(MANAGER_WHITE_LIST).hasRole("MANAGER")
                                .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(authenticationEntryPoint));
        return http.build();
    }

    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs*/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] WHITE_LIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/v2/api-docs",
            "/swagger-ui/index.html",
            "/configuration/**",
            "/actuator/**",
            "/api/v1/auth/login",
            "/api/v1/auth/initiate-account-verification",
            "/api/v1/auth/verify-account/**",
            "/api/v1/auth/initiate-reset-password",
            "/api/v1/auth/reset-password",
    };
    private static final String[] ADMIN_WHITE_LIST = {
            "/api/v1/admins/**",
            "/api/v1/auth/register/manager",
            "/api/v1/deductions/**",
            "/api/v1/payslips/approve/**"
    };
    private static final String[] ADMIN_MANAGER_WHITE_LIST = {

            "/api/v1/auth/register",
            "/api/v1/payslips/all/**"

    };
    private static final String[] MANAGER_WHITE_LIST = {
            "/api/v1/payslips/generate",
    };

}
