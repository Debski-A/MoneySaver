package com.debski.authservice.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourcesConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource accountDataSource() {
        DataSource accountDBDS = DataSourceBuilder.create().build();
        return accountDBDS;
    }

    @Bean
    @ConfigurationProperties(prefix="spring.token-datasource")
    public DataSource tokenDataSource() {
        DataSource tokenDBDS = DataSourceBuilder.create().build();
        return tokenDBDS;
    }
}
