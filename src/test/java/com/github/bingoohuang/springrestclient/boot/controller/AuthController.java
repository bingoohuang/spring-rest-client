package com.github.bingoohuang.springrestclient.boot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2016/11/25.
 */
@RestController @RequestMapping("/auth") public class AuthController {
    @RequestMapping("/hello") public String hello() {
        return "world";
    }

    @RequestMapping("/world") public String world() {
        return "hello";
    }
}
