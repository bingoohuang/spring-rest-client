package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.provider.DefaultSignProvider;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RequestMapping("/get")
@SpringRestClientEnabled(baseUrl = "http://localhost:4849",
    signProvider = DefaultSignProvider.class)
public interface GetApi {
    @RequestMapping(value = "/error/{error}", method = GET)
    int exception(@PathVariable("error") int error);
}
