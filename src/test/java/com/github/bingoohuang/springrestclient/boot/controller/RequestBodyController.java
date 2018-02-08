package com.github.bingoohuang.springrestclient.boot.controller;

import com.github.bingoohuang.springrestclient.boot.domain.Account;
import com.github.bingoohuang.springrestclient.boot.domain.Car;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/RequestBody")
public class RequestBodyController {
    @RequestMapping(value = "/transfer", method = POST)
    public String transfer(@RequestBody Account fromAccount, @RequestBody Car car) {
        return "account:" + fromAccount + ", car:" + car;
    }
}
