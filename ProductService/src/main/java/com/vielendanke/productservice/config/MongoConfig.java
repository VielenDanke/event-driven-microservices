package com.vielendanke.productservice.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
public class MongoConfig {

    private final Environment environment;

    @Autowired
    public MongoConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public MongoClientSettings mongoSettings() {
        return MongoClientSettings
                .builder()
                .applyConnectionString(new ConnectionString(
                        Objects.requireNonNull(environment.getProperty("mongodb.connection.url"))))
                .applicationName("product-service")
                .build();
    }

    @Bean
    public MongoClient mongoClient(@Autowired MongoClientSettings settings) {
        return MongoClients.create(settings);
    }
}
