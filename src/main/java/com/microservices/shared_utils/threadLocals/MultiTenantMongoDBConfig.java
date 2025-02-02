package com.microservices.shared_utils.threadLocals;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class MultiTenantMongoDBConfig {

    private final Map<String, MongoTemplate> templateCache = new ConcurrentHashMap<>();
    private final Map<String, MongoClient> clientCache = new ConcurrentHashMap<>();

    public MongoTemplate getMongoTemplate() {
        String connectionString = MongoConnectionStorage.getConnection();
        return templateCache.computeIfAbsent(connectionString, this::createMongoTemplate);
    }

    private MongoTemplate createMongoTemplate(String connectionString) {
        ConnectionString connString = new ConnectionString(connectionString);
        MongoClient mongoClient = clientCache.computeIfAbsent(connectionString, MongoClients::create);
        String databaseName = connString.getDatabase();
        if (databaseName == null || databaseName.isEmpty()) {
            throw new IllegalStateException("Database name not specified in the connection string");
        }
        System.out.println("Creating new MongoTemplate for: " + connectionString);
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient, databaseName));
    }

    @PreDestroy
    public void cleanUp() {
        clientCache.values().forEach(MongoClient::close);
        clientCache.clear();
        templateCache.clear();
    }
}