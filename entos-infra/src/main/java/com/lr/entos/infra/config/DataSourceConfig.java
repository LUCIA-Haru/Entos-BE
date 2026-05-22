package com.lr.entos.infra.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public HikariDataSource dataSource(DataSourceProperties properties) {
        String url = properties.getUrl();

        // Priority 1: Read from Render environment variables (DB_URL)
        if (url == null || url.isEmpty() || !url.startsWith("jdbc:")) {
            url = System.getenv("DB_URL");
        }

        // Final fallback (for local development)
        if (url == null || url.isEmpty() || !url.startsWith("jdbc:")) {
            url = "jdbc:postgresql://localhost:5432/entos_db";
        }

        HikariDataSource dataSource = properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();

        dataSource.setJdbcUrl(url);
        return dataSource;
    }
}

