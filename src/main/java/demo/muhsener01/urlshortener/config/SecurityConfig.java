package demo.muhsener01.urlshortener.config;

import demo.muhsener01.urlshortener.domain.entity.Role;
import demo.muhsener01.urlshortener.repository.RoleRepository;
import demo.muhsener01.urlshortener.repository.impl.UserRepositoryImpl;
import demo.muhsener01.urlshortener.security.SecurityConstants;
import demo.muhsener01.urlshortener.security.filter.JwtAuthenticationFilter;
import demo.muhsener01.urlshortener.security.handler.CustomAuthEntryPoint;
import demo.muhsener01.urlshortener.security.handler.CustomAuthFailureHandler;
import demo.muhsener01.urlshortener.security.handler.CustomAuthenticationSuccessHandler;
import demo.muhsener01.urlshortener.service.CacheService;
import demo.muhsener01.urlshortener.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    private final RoleRepository roleRepository;
    private final SecurityConstants securityConstants;
    private final UserRepositoryImpl userRepositoryImpl;
    private final CacheService<String, Role> roleCacheService;
    private final ApplicationProperties applicationProperties;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public UserDetailsService userDetailsService() {
        return new UserServiceImpl(passwordEncoder(), roleRepository, userRepositoryImpl, roleCacheService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder builder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = builder.build();


        UsernamePasswordAuthenticationFilter loginFilter = new UsernamePasswordAuthenticationFilter();
        loginFilter.setFilterProcessesUrl(securityConstants.getLoginUrl());
        loginFilter.setAuthenticationManager(authenticationManager);
        loginFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler(securityConstants));
        loginFilter.setAuthenticationFailureHandler(new CustomAuthFailureHandler());
        loginFilter.afterPropertiesSet();


        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authenticationManager(authenticationManager)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(new RateLimitFilter(), DisableEncodeUrlFilter.class)
                .addFilter(loginFilter)
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager, new CustomAuthEntryPoint(), securityConstants), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(applicationProperties.getBaseDomain()));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
