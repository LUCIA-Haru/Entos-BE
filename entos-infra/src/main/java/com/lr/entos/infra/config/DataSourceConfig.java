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

        // ⚡ Look for Render's direct raw injection fallback
        if (url == null || url.isEmpty() || !url.startsWith("jdbc:")) {
            String renderUrl = System.getenv("SPRING_DATASOURCE_URL");
            if (renderUrl != null && !renderUrl.isEmpty()) {
                url = "jdbc:" + renderUrl;
            }
        }

        // If it still doesn't have jdbc: (e.g. running empty in compilation), force a dummy safe fallback
        if (url == null || !url.startsWith("jdbc:")) {
            url = "jdbc:postgresql://localhost:5432/entos_db";
        }

        HikariDataSource dataSource = properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();

        dataSource.setJdbcUrl(url);
        return dataSource;
    }
}
