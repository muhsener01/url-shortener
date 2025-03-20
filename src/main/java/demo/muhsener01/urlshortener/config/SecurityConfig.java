package demo.muhsener01.urlshortener.config;

import demo.muhsener01.urlshortener.repository.RoleRepository;
import demo.muhsener01.urlshortener.repository.UserRepository;
import demo.muhsener01.urlshortener.security.SecurityConstants;
import demo.muhsener01.urlshortener.security.filter.JwtAuthenticationFilter;
import demo.muhsener01.urlshortener.security.handler.CustomAuthFailureHandler;
import demo.muhsener01.urlshortener.security.handler.CustomAuthenticationSuccessHandler;
import demo.muhsener01.urlshortener.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SecurityConstants securityConstants;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public UserDetailsService userDetailsService() {
        return new UserServiceImpl(userRepository, passwordEncoder(), roleRepository);
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
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(HttpMethod.POST, "/api/v1/auth/sign-up").permitAll()
                                .requestMatchers(HttpMethod.GET, "/signup").permitAll()
                                .requestMatchers(HttpMethod.POST, "/signup").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers(HttpMethod.GET, "/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authenticationManager(authenticationManager)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(loginFilter)
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager, securityConstants), BasicAuthenticationFilter.class)
                .build();
    }
}
