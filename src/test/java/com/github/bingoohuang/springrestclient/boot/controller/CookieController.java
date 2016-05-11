package com.github.bingoohuang.springrestclient.boot.controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CookieController {
    @RequestMapping("/cookie/hello")
    public String hello(@CookieValue("foo") String fooCookie) {
        return "hello:" + fooCookie;
    }
}
