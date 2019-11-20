package com.test.controller;


import com.test.bean.DemoUser;
import com.test.service.MongoService;
import com.zzy.vertx.core.annotaion.AsyncHandler;
import io.netty.util.internal.ObjectUtil;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.Lock;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import net.ipip.ipdb.City;
import net.ipip.ipdb.CityInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.multipart.MultipartFile;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

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
        logger.info(Thread.currentThread().getName());
        logger.info("test2");
        EventBus eventBus = vertx.eventBus();
        eventBus.request("testeb", "你好啊", messageAsyncResult -> {
            if(messageAsyncResult.succeeded()){
                context.response().end((String)messageAsyncResult.result().body());
            }else {
                context.response().end("not found");
            }
        });
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
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("testeb", message -> {
            message.reply(Json.encode("gunnima"));
            logger.info("testeb   gunnima");
        });
//        Thread.sleep(50);
        DemoUser user = new DemoUser();
        user.setId("q111");
        return user;

//        return mongoService.getMusicsSync();
    }

//    @ResponseBody
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
        fileName = UUID.randomUUID()+suffixName;
        //指定本地文件夹存储图片
        String filePath = "D:/aa/";
        try {
            //将图片保存到static文件夹里
            FileUtils.copyFile(new File(upload.uploadedFileName()), new File(filePath+fileName));
            return mongoService.getMusicsSync();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    @RequestMapping(value = "/test5", method = RequestMethod.PUT, produces = "application/xml")
    public List<DemoUser> get5(@RequestBody DemoUser user) throws Exception {

        logger.info(Thread.currentThread().getName());
        logger.info("test5");
        DemoUser demoUser = new DemoUser();
        demoUser.setId("2222");
        List list = new ArrayList();
        list.add(user);
        list.add(demoUser);
        return list;

    }

    @AsyncHandler
    @RequestMapping(value = "/test7", method = RequestMethod.GET, produces = "application/xml")
    public void get55(RoutingContext context) throws Exception {
        DemoUser demoUser = new DemoUser();
        demoUser.setId("2222");
        List list = new ArrayList();
        list.add(demoUser);
        context.response().end(Json.encodeToBuffer(list));

    }

//    @Async
    @GetMapping(value = "/ip/:ip")
    public String get6(String ip) throws Exception {
            if(city == null){
                InputStream stream = this.getClass().getClassLoader().getResourceAsStream("ipipfree.ipdb");
                city = new City(stream);
            }
            CityInfo info = city.findInfo(ip+"", "CN");
            return info.getCityName();
    }
}
