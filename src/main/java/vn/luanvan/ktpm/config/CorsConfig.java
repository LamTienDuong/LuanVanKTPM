package vn.luanvan.ktpm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // cho phep cac URL nao co the ket noi toi backend
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://localhost:4173", "http://localhost:5173"));
        // cac method nao duoc ket noi
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE",
                "OPTIONS"));
        // cac phan header duoc phep gui len
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type",
                "Accept", "x-no-retry"));
        // gui kem cookie hay khong
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        // Thoi gian pre-flight request co the cache tinh theo second
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply this configuration to all paths
        return source;
    }
}
