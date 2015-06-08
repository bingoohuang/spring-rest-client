package com.github.bingoohuang.springrestclient.boot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/another-controller")
public class AnotherController {
    @RequestMapping("/add")
    public int add(@RequestParam("offset") int offset, HttpServletResponse response) {
        response.addHeader("fuck", "you" + offset);
        return offset ;
    }
}
