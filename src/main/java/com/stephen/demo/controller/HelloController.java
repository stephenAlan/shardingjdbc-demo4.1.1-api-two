package com.stephen.demo.controller;

import com.sun.deploy.net.HttpResponse;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="shishouchao@foresee.com.cn">shouchao.shi</a>
 * @version 1.0.0
 * @date 2023/06/07 17:31
 * @Description
 */
@RestController
public class HelloController {

    @RequestMapping("hello")
    public String hello(HttpRequest request, HttpResponse response) {
        return "hello shardingjdbc";
    }


}
