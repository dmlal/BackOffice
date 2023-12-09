package com.sparta.backoffice.global.config;

import com.sparta.backoffice.auth.repository.LogoutRepository;
import com.sparta.backoffice.global.security.CustomUserDetailService;
import com.sparta.backoffice.global.security.JwtAuthorizationFilter;
import com.sparta.backoffice.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.stream.Stream;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    private final AccessDeniedHandler customAccessDeniedHandler;
    private final AuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomUserDetailService userDetailService;
    private final LogoutRepository logoutRepository;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        //Csrf disable
        http.csrf(AbstractHttpConfigurer::disable);
        //FormLogin disable
        http.formLogin(AbstractHttpConfigurer::disable);
        //Session Stateless
        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //url permit
        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers(this.whiteListMapToMvcRequestMatchers(mvc)).permitAll() //허용 url 리스트
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/read/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/follows/**").hasAnyRole("USER", "ADMIN", "BLOCK")
                        .requestMatchers("/api/follows/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/likes/**").hasAnyRole("USER", "ADMIN", "BLOCK")
                        .requestMatchers("/api/likes/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/posts/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
        );

        // 필터 관리
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handler -> handler.accessDeniedHandler(customAccessDeniedHandler))
                .exceptionHandling(handler -> handler.authenticationEntryPoint(customAuthenticationEntryPoint));

        return http.build();
    }

    private MvcRequestMatcher[] whiteListMapToMvcRequestMatchers(MvcRequestMatcher.Builder mvc) {
        return Stream.of(WHITE_LIST_URL).map(mvc::pattern).toArray(MvcRequestMatcher[]::new);
    }

    @Bean
    public MvcRequestMatcher.Builder mvcRequestMatcherBuilder(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtProvider, userDetailService, logoutRepository);
    }

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/api/v1/member/signup",
            "/v1/api-docs/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };
}
