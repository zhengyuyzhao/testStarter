package com.test.db;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(mongoconfig.class)
public class MongodbClientConfig {
    @Autowired
    private mongoconfig mongoconfig;
    public MongodbClientConfig(){
//        RedisClient
    }

    @Bean
    MongoClient mongodbClient(){
        JsonObject config = new JsonObject();
        config.put("host", mongoconfig.getHost());
        config.put("port", mongoconfig.getPort());
        config.put("username", mongoconfig.getUsername());
        config.put("password", mongoconfig.getPassword());
        config.put("db_name", mongoconfig.getAuthSource());

        MongoClient client = MongoClient.createShared(Vertx.vertx(), JsonObject.mapFrom(config));
        return client;
    }
}
