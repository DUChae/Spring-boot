package com.example.demo;

import com.example.demo.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- API 전용 SecurityFilterChain (우선순위 높음) ---
    @Bean
    @Order(1) // 우선순위 1
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable()) // ✅ CSRF 비활성화는 그대로 유지
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/**").permitAll() // ✅ POST 요청 명시적 허용
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()  // ✅ GET 요청 명시적 허용
                        .anyRequest().authenticated() // 그 외 API 요청은 인증 필요
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());
        return http.build();
    }

    // --- 웹(Web UI)용 SecurityFilterChain (우선순위 낮음) ---
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth
                        // 웹 페이지 접근 허용 규칙
                        .requestMatchers(
                                "/",
                                "/posts",
                                "/posts/{id}",
                                "/users/register",
                                "/login",
                                "/css/**",
                                "/js/**").permitAll()
                        .anyRequest().authenticated() // 그 외 모든 웹 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/posts", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
