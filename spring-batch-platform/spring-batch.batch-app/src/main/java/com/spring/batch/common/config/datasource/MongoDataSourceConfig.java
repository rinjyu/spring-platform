package com.spring.batch.common.config.datasource;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@RefreshScope
@Configuration
public class MongoDataSourceConfig {

    @Primary
    @Bean(name = "mongoProperties")
    @ConfigurationProperties(prefix = "spring.mongo.datasource")
    public MongoProperties getMongoProperties() {
        return new MongoProperties();
    }

    @Primary
    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDatabaseFactory(getMongoProperties()));
    }

    @Primary
    @Bean(name = "mongoDatabaseFactory")
    public MongoDatabaseFactory mongoDatabaseFactory(MongoProperties mongoProperties) {
        MongoClient mongoClient = MongoClients.create(mongoProperties.getUri());
        return new SimpleMongoClientDatabaseFactory(mongoClient, mongoProperties.getDatabase());
    }
}
