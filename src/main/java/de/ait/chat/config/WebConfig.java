package de.ait.chat.config;

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
                // Разрешаем доступ к API
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:8080","http://localhost:3000", "http://localhost:3001", "http://localhost:3002", "http://localhost:3003", "http://localhost:3005", "http://localhost:5500")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("Authorization", "Content-Type", "Accept")
                        .allowCredentials(true);

                // Разрешаем доступ к статическим ресурсам
                registry.addMapping("/**") // Здесь вы можете указать более конкретные маршруты, если хотите
                        .allowedOrigins("http://localhost:8080","http://localhost:3000", "http://localhost:3001", "http://localhost:3002", "http://localhost:3003", "http://localhost:3005", "http://localhost:5500")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*") // Разрешаем все заголовки
                        .allowCredentials(true);
            }
        };
    }
}
