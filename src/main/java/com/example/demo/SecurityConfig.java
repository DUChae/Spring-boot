package com.example.demo;

import com.example.demo.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth
                        // 1. 로그인해야만 접근 가능한 경로 (가장 구체적인 규칙을 먼저 배치)
                        .requestMatchers(
                                "/posts/new",
                                "/posts/{id}/edit",
                                "/posts/{id}/delete",
                                "/posts/{id}/like"
                        ).authenticated()

                        // 2. 로그인 없이 접근 가능한 경로
                        .requestMatchers(
                                "/posts",
                                "/posts/{id}",
                                "/users/register",
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/api/posts/**"
                        ).permitAll()

                        // 3. 위 규칙에 해당하지 않는 모든 나머지 요청은 로그인 필요
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/posts", true)
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}