package com.test;

import com.zzy.vertx.core.handler.VertxHandlerInterceptor;
import com.zzy.vertx.core.handler.VertxHandlerInterceptorManager;
import com.zzy.vertx.core.webconfig.VertxWebConfigSupport;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class WebMvcConfig extends VertxWebConfigSupport {
    @Autowired
    private Vertx vertx;
    @Override
    protected void addInterceptors(VertxHandlerInterceptorManager interceptorManager) {
        interceptorManager.addInterceptor(new VertxHandlerInterceptor() {
            @Override
            public boolean preHandle(RoutingContext context, Object handler) throws Exception {
//                System.out.println("aaaa");
                return true;
            }
        });
        super.addInterceptors(interceptorManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public Router router2(){
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.errorHandler(404, ctx ->{
            ctx.response().setStatusCode(404).end("404 ...");
        });
        router.errorHandler(500, ctx ->{
            ctx.response().end(ctx.failure().getMessage());
        });

        return router;
    }
}
