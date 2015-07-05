package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.provider.DefaultSignProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/mix-query-and-fields-controller")
@SpringRestClientEnabled(baseUrl = "http://localhost:4849", signProvider = DefaultSignProvider.class)
public interface MixQueryAndFieldsApi {
    @RequestMapping("/case1")
    String case1(@RequestParam("q^name") String name,
                 @RequestParam("q^addr") String addr,
                 @RequestParam("age") int age);
}
