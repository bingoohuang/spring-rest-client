package com.github.bingoohuang.springrestclient.spring.demo;

import com.github.bingoohuang.springrestclient.spring.api.AnotherApi;
import com.github.bingoohuang.springrestclient.utils.UniRests;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;

public class AnotherApiDemo implements AnotherApi {
    @Override
    public int add(@RequestParam("offset") int offset) {
        LinkedHashMap pathVariables = new LinkedHashMap();
        LinkedHashMap requestParams = new LinkedHashMap();
        requestParams.put("offset", offset);
        return Integer.valueOf(UniRests.post("url", pathVariables, requestParams));
    }
}
