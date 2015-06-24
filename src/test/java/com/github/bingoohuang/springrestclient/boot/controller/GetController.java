package com.github.bingoohuang.springrestclient.boot.controller;

import com.github.bingoohuang.springrest.boot.annotations.RestfulSign;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/get")
@RestfulSign
public class GetController {
    @RequestMapping(value = "/error/{error}", method = GET)
    int exception(@PathVariable("error") int error) {
        return error;
    }
}
