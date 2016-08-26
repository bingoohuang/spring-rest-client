package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.boot.domain.Sex;
import com.github.bingoohuang.springrestclient.provider.DefaultSignProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/enum")
@SpringRestClientEnabled(baseUrl = "http://localhost:4849",
    signProvider = DefaultSignProvider.class)
public interface EnumApi {
    @RequestMapping("/test1")
    String test1(@RequestParam("sex") Sex sex);
}
