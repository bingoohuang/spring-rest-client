package com.github.bingoohuang.springrestclient.boot.controller;

import com.github.bingoohuang.springrest.boot.annotations.RestfulSign;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RestfulSign
@RequestMapping("/mix-query-and-fields-controller")
public class MixQueryAndFieldsController {
    @RequestMapping("/case1")
    public String case1(@RequestParam("name") String name,
                        @RequestParam("addr") String addr,
                        @RequestParam("age") int age) {
        return name + "," + addr + "," + age;
    }
}
