package com.example.rental;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public Dotenv dotenv() {
        Dotenv dotenv = Dotenv.load();
        logger.info("DATABASE_URL: {}", dotenv.get("DATABASE_URL"));
        logger.info("DATABASE_USERNAME: {}", dotenv.get("DATABASE_USERNAME"));
        logger.info("DATABASE_PASSWORD: {}", dotenv.get("DATABASE_PASSWORD"));
        return dotenv;
    }
}
