package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.provider.DefaultSignProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringRestClientEnabled(baseUrl = "http://localhost:4849", signProvider = DefaultSignProvider.class)
@RequestMapping("/not-required")
public interface NotRequiredApi {
    @RequestMapping("/test1")
    String test1(@RequestParam("name") String name);

    @RequestMapping("/test1")
    String test2();
}