package com.ost.back_testing_back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")  // 1️⃣ 이 URL 패턴으로 오는 요청에 대해
                        .allowedOrigins("http://localhost:5173")  // 2️⃣ 이 origin에서 오는 요청을 허용
                        .allowedMethods("*")  // 3️⃣ GET, POST, PUT, DELETE 등 모든 HTTP 메서드 허용
                        .allowedHeaders("*"); // 4️⃣ 모든 요청 헤더 허용
            }
        };
    }
}