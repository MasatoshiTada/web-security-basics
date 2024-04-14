package com.example.todobackend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
// @EnableWebMvcを付けるとSpring Bootデフォルトの設定が無効になるので付けない
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:9080")
                .allowCredentials(true)
                .allowedMethods("PUT", "DELETE", "GET", "POST", "PATCH", "OPTIONS")
                .allowedHeaders("Content-Type", "X-CSRF-TOKEN")
                .exposedHeaders("*")
                .maxAge(7200);
    }
}
