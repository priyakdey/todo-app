package io.todo.api.security;

import io.todo.api.security.jwt.JwtBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class is a Configuration class Web Security related Bean Definitions
 */
@Configuration
public class AppSecurityBeanConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConfigurationProperties(prefix = "jwt.token")
    public JwtBean jwtBean() {
        return new JwtBean();
    }
}
