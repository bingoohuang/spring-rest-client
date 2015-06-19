package com.github.bingoohuang.springrestclient.boot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tid")
public class TidController {
    @RequestMapping("/get-mobile")
    String getMobile(@RequestParam("tid") String tid) {
        return "bingoo:" + tid;
    }
}
