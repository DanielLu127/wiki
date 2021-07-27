package com.daniel.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestController {

    @Value("${test.hello:Test}") //读取配置项,冒号后面是默认值
    private String testHello;
    /*
    @RequestMapping(value = "xxx") 支持GET,POST,PUT,DELTE等所有请求
    @RequestMapping(value = "xxx", method = xxx) 支持method所选的请求
    @GETMapping仅支持GET请求
    @POSTMapping仅支持POST
    @PUTMapping仅支持
    @DeleteMapping仅支持Delete
     */

    @RequestMapping(value = "/hello")
    public String hello() {
        return "Hello Wolrd" + testHello;
    }
}
