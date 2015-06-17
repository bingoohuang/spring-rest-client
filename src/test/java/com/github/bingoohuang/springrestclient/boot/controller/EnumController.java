package com.github.bingoohuang.springrestclient.boot.controller;

import com.github.bingoohuang.springrestclient.boot.domain.Sex;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enum")
public class EnumController {
    @RequestMapping("/test1")
    public String test1(@RequestParam("sex") Sex sex) {
        return sex.toString();
    }
}
