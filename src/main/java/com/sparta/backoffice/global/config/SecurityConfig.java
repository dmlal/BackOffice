package com.sparta.backoffice.global.config;

import java.util.stream.Stream;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.sparta.backoffice.auth.repository.LogoutRepository;
import com.sparta.backoffice.global.security.CustomAuthenticationEntryPoint;
import com.sparta.backoffice.global.security.oauth.CustomOAuth2UserService;
import com.sparta.backoffice.global.security.CustomUserDetailService;
import com.sparta.backoffice.global.security.JwtAuthorizationFilter;
import com.sparta.backoffice.global.security.oauth.OAuth2AuthenticationFailureHandler;
import com.sparta.backoffice.global.security.oauth.OAuth2AuthenticationSuccessHandler;
import com.sparta.backoffice.global.util.JwtProvider;
import com.sparta.backoffice.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    private final AccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final OAuth2AuthenticationSuccessHandler oauth2SuccessHandler;
    private final OAuth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;
    private final CustomOAuth2UserService oAuth2UserService;
    private final CustomUserDetailService userDetailService;
    private final LogoutRepository logoutRepository;
    private final UserRepository userRepository;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        //Csrf disable
        http.csrf(AbstractHttpConfigurer::disable);
        //FormLogin disable
        http.formLogin(AbstractHttpConfigurer::disable);
        //Session Stateless
        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.httpBasic(AbstractHttpConfigurer::disable);

        http
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(endpointConfig -> endpointConfig.baseUri("/api/auth/login"))
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2UserService))
                .successHandler(oauth2SuccessHandler)
                .failureHandler(oauth2AuthenticationFailureHandler)
            );
        //url permit
        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers(this.whiteListMapToMvcRequestMatchers(mvc)).permitAll() //허용 url 리스트
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
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
    public CustomOAuth2UserService oAuth2UserService() {
        return new CustomOAuth2UserService(userRepository);
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
