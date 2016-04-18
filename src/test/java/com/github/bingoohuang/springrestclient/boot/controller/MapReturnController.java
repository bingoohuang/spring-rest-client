package com.github.bingoohuang.springrestclient.boot.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;

@RestController
public class MapReturnController {
    @RequestMapping("/mapreturn")
    public Map<String, String> showme() {
        return of("name", "bingoo", "department", "ebs");
    }
}
