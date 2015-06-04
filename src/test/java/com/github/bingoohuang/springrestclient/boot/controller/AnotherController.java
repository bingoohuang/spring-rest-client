package com.github.bingoohuang.springrestclient.boot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/another")
public class AnotherController {
    @RequestMapping("/add")
    public int add(@RequestParam("offset") int offset) {
        return offset ;
    }
}
