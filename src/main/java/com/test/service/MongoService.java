package com.test.service;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@ConditionalOnBean(MongoClient.class)
//@Transactional(rollbackFor = Exception.class)
public class MongoService {
    private static final Logger logger = LoggerFactory.getLogger(MongoService.class);
    @Autowired
    private MongoClient client;

    public void getMusics(RoutingContext context) throws Exception {
        client.find("music", new JsonObject(), res -> {
//            logger.info(Thread.currentThread().getName());
            if(res.succeeded())
            context.response().end(Json.encodeToBuffer(res.result().get(0)));
            else context.response().end("error");
        });
    }

    public void setMusics(RoutingContext context) throws Exception {
        JsonObject object = new JsonObject();
        object.put("id", "0");
        object.put("name","0");
        client.insert("music", object, stringAsyncResult -> {
            context.response().end(stringAsyncResult.result());
        });
    }

    public Object getMusicsSync() throws Exception{
        CompletableFuture future = new CompletableFuture();
//        logger.info(Thread.currentThread().getName());
        client.find("music", new JsonObject(), res -> {
//            logger.info("thread:=={}", Thread.currentThread());
            if(res.succeeded()){
                future.complete(res.result());
            }else {
                future.obtrudeException(res.cause());
            }

        });
//        throw new Exception();
        return future.get();
    }
}
