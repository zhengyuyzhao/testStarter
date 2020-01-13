package com.test.controller;


import com.test.bean.DemoUser;
import com.test.bean.Student;
import com.test.service.MongoService;
import com.zzy.vertx.core.annotaion.AsyncHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.dns.AddressResolverOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.Lock;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.redis.RedisClient;
import net.ipip.ipdb.City;
import net.ipip.ipdb.CityInfo;
import org.apache.commons.io.FileUtils;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;

@Validated
@RestController
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    @Autowired
    private Vertx vertx;

    @Autowired
    private MongoService mongoService;

    private City city;

//    @Autowired
//    H2Service h2Service;

    @AsyncHandler
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void get(RoutingContext context) throws Exception {
        logger.info(Thread.currentThread().getName());
        logger.info("test");
        SharedData sharedData = vertx.sharedData();
        CompletableFuture<Lock> future = new CompletableFuture();
        sharedData.getLock("qqq", lockAsyncResult -> {
            if (lockAsyncResult.succeeded()) {
                future.complete(lockAsyncResult.result());
            } else {
                future.completeExceptionally(lockAsyncResult.cause());
            }
        });
        Lock lock = future.get();
        CompletableFuture<AsyncMap> mapFuture = new CompletableFuture();
        sharedData.getAsyncMap("map", asyncMapAsyncResult -> {
            if (asyncMapAsyncResult.succeeded()) {
                mapFuture.complete(asyncMapAsyncResult.result());
            } else {
                mapFuture.completeExceptionally(asyncMapAsyncResult.cause());
            }
        });
        AsyncMap asyncMap = mapFuture.get();
        Random random = new Random();
        asyncMap.put("bbb", random.nextInt(100), new Handler<AsyncResult<Void>>() {
            @Override
            public void handle(AsyncResult<Void> voidAsyncResult) {
                if (voidAsyncResult.succeeded()) {
                    asyncMap.entries(new Handler<AsyncResult<Map>>() {
                        @Override
                        public void handle(AsyncResult<Map> mapAsyncResult) {
                            context.response().end(Json.encodeToBuffer(mapAsyncResult.result()));


                        }
                    });
                } else {
                    context.response().end();
                }
            }
        });
        lock.release();
    }

    @AsyncHandler
    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public void get2(RoutingContext context) throws Exception {
//        logger.info(Thread.currentThread().getName());
//        logger.info("test2");
//        EventBus
//        EventBusOptions options = new EventBusOptions();
//        VertxOptions vertxOptions = new VertxOptions();
//        vertxOptions.setAddressResolverOptions(new AddressResolverOptions());
//        EventBus eventBus = vertx.eventBus();
//        eventBus.request("testeb", "你好啊", messageAsyncResult -> {
//            if (messageAsyncResult.succeeded()) {
//                context.response().end((String) messageAsyncResult.result().body());
//            } else {
//                context.response().end("not found");
//            }
//        });
        try{
            throw new Error(" eee");
        }catch (Error e){
            System.out.println(e.getMessage());
        }

//        Map map = new HashMap();
//        map.put("aaa", "aaa");
//        context.response().end(Json.encodeToBuffer(map));
//        mongoService.getMusics(context);
    }

    @RequestMapping(value = "/test3", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE})
    public Object get3() throws Exception {
//        Map map = new HashMap();
//        map.put("aaa","aaa");
        logger.info(Thread.currentThread().getName());
        logger.info("test3");
//        EventBus eventBus = vertx.eventBus();
//        eventBus.consumer("testeb", message -> {
//            message.reply(Json.encode("gunnima"));
////            logger.info("testeb   gunnima");
//        });
////        Thread.sleep(50);
//        DemoUser user = new DemoUser();
//        user.setId("q111");
//        return user;

        return mongoService.getMusicsSync();
    }

        @ResponseBody
    @PostMapping(value = "/test4")
    public Object get4(RoutingContext context) throws Exception {
//        Map map = new HashMap();
//        map.put("aaa","aaa");
        logger.info(Thread.currentThread().getName());
        logger.info("test4");

        FileUpload upload = context.fileUploads().iterator().next();
        String fileName = upload.fileName();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //重新生成文件名
        fileName = UUID.randomUUID() + suffixName;
        //指定本地文件夹存储图片
        String filePath = "D:/aa/";
        try {
            //将图片保存到static文件夹里
            FileUtils.copyFile(new File(upload.uploadedFileName()), new File(filePath + fileName));
            return mongoService.getMusicsSync();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    @RequestMapping(value = "/test5", method = RequestMethod.POST, produces = "application/xml")
    public List<DemoUser> get5(@Valid @RequestBody DemoUser user) throws Exception {

//        logger.info(Thread.currentThread().getName());
//        logger.info("test5");
        DemoUser demoUser = new DemoUser();
        demoUser.setId("2222");
        List list = new ArrayList();
        list.add(user);
        list.add(demoUser);

        return list;

    }

    @AsyncHandler
    @ResponseBody
    @RequestMapping(value = "/test7", method = RequestMethod.GET, produces = "application/xml")
    public void get55(RoutingContext context) throws Exception {
        DemoUser demoUser = new DemoUser();
        demoUser.setId("2222");
        List list = new ArrayList();
        list.add(demoUser);
        context.response().end(Json.encodeToBuffer(list));
        DeferredResult result = new DeferredResult();
        result.getResult();
    }

    //    @Async
    @GetMapping(value = "/ip/:ip")
    public String get6(String ip) throws Exception {
        if (city == null) {
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream("ipipfree.ipdb");
            city = new City(stream);
        }
        CityInfo info = city.findInfo(ip + "", "CN");
        return info.getCityName();
    }

    @RequestMapping(value = "/test19", method = RequestMethod.POST)
    public String get9(@RequestBody @Valid Student student) throws Exception {

        return "aaa";
    }

    @AsyncHandler
    @RequestMapping(value = "/test11", method = RequestMethod.GET)
    public void get11(RoutingContext context, @NotNull String id) throws Exception {

        mongoService.setMusics(context);
    }
    @AsyncHandler
    @RequestMapping(value = "/test12", method = RequestMethod.GET)
    public void get12(RoutingContext context, @NotNull String id) throws Exception {

        mongoService.getMusics(context);
    }
}
