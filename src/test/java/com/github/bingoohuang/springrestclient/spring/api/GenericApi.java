package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/generic")
@SpringRestClientEnabled(baseUrl = "http://localhost:4849")
public interface GenericApi {

    @RequestMapping("/map")
    Map<String, String> map();
}
