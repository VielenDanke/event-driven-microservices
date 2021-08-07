package com.vielendanke.orderservice.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.jsr310.Jsr310CodecProvider;
import org.bson.codecs.pojo.PojoCodecProvider;
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

    @Bean("shopDatabase")
    public MongoDatabase shopDatabase(@Autowired MongoClient mongoClient) {
        return mongoClient.getDatabase("shop");
    }

    @Bean
    public MongoClientSettings mongoSettings() {
        return MongoClientSettings
                .builder()
                .applyConnectionString(new ConnectionString(
                        Objects.requireNonNull(environment.getProperty("mongodb.connection.url"))))
                .credential(MongoCredential.createCredential(
                        Objects.requireNonNull(environment.getProperty("MONGODB_USERNAME")),
                        Objects.requireNonNull(environment.getProperty("MONGODB_DATABASE")),
                        Objects.requireNonNull(environment.getProperty("MONGODB_PASSWORD")).toCharArray()
                ))
                .applicationName("order-service")
                .codecRegistry(CodecRegistries.fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        CodecRegistries.fromProviders(
                                PojoCodecProvider.builder().automatic(true).build(),
                                new ValueCodecProvider(),
                                new Jsr310CodecProvider()
                        ))
                )
                .build();
    }

    @Bean
    public MongoClient mongoClient(@Autowired MongoClientSettings settings) {
        return MongoClients.create(settings);
    }
}
