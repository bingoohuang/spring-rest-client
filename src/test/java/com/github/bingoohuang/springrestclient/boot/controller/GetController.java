package com.github.bingoohuang.springrestclient.boot.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/get")
public class GetController {
    @RequestMapping(value = "/error/{error}", method = RequestMethod.GET)
    int exception(@PathVariable("error") int error) {
        return error;
    }
}
