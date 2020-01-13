package com.test;

import com.zzy.vertx.core.handler.VertxHandlerInterceptor;
import com.zzy.vertx.core.handler.VertxHandlerInterceptorManager;
import com.zzy.vertx.core.webconfig.VertxWebConfigSupport;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


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
    public Router router2(){
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.errorHandler(404, ctx ->{
            ctx.response().setStatusCode(404).end("404 ...");
        });
        router.errorHandler(500, ctx ->{
            ctx.response().end(ctx.failure().getMessage());
        });
        router.route("/test9").handler(routingContext -> routingContext.response().end("HELLO WORLD"));

        return router;
    }

//    @Bean
    /**
     * Method:  开启快速返回
     * Description:
     *          如果参数校验有异常，直接抛异常，不会进入到 controller，使用全局异常拦截进行拦截
     * Author: liu kai
     * Date: 2018/7/12 17:33
     *
     * @param
     * @return org.springframework.validation.beanvalidation.MethodValidationPostProcessor
     */
//    public MethodValidationPostProcessor methodValidationPostProcessor() {
//        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
//        /**设置validator模式为快速失败返回*/
//        postProcessor.setValidator(validator());
//        return postProcessor;
//    }
//
//    @Bean
//    public Validator validator(){
//        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
//                .configure()
//                .addProperty( "hibernate.validator.fail_fast", "true" )
//                .buildValidatorFactory();
//        Validator validator = validatorFactory.getValidator();
//        return validator;
//    }
}
