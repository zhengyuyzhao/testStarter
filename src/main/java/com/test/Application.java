package com.test;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Semaphore;

@SpringBootApplication
public class Application {

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
//        Vertx vertx = Vertx.vertx();
//        DeploymentOptions options = new DeploymentOptions().setInstances(2);
//        Router router = Router.router(vertx);
//        router.route("/test").handler(context1 -> {
//            System.out.println(Thread.currentThread().getName());
//            context1.response().end(Thread.currentThread().getName());
//        });
////        vertx.deployVerticle(verticle2.class.getName(), options);
////        vertx.deployVerticle(Verticel1.class.getName(), options);
//        vertx.createHttpServer().requestHandler(router).listen(8004);
    }

}
