package com.github.bingoohuang.springrestclient.boot.controller;

import com.github.bingoohuang.springrestclient.boot.annotations.RestfulSign;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/not-required")
@RestfulSign(ignore = true)
public class NotRequiredController {
    @RequestMapping("/test1")
    public String test1(@RequestParam(value = "name", required = false) String name) {
        return name + System.currentTimeMillis();
    }
}
