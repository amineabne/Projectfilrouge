package com.example.projectfilrouge.security;

import com.example.projectfilrouge.security.jwt.AuthEntryPoint;
import com.example.projectfilrouge.security.jwt.AuthTokenFilter;
import com.example.projectfilrouge.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthEntryPoint unauthorizedHandler;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {//donner les infos de l'authentification
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);//userservice implement userdetailservice et c'est ça qui intéresse setuserdetailservice
        authProvider.setPasswordEncoder(passwordEncoder);//Fait appel au password encoder pour voir si sa match avec le password utilisé par le user

        return authProvider;
    }

    @Bean
    public AuthTokenFilter authenticationJwTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/registration/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/tickets/**")
                                .permitAll()
                                .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}