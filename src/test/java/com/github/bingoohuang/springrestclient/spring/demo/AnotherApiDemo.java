package com.github.bingoohuang.springrestclient.spring.demo;

import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;
import com.github.bingoohuang.springrestclient.spring.api.AnotherApi;
import com.github.bingoohuang.springrestclient.utils.UniRests;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.Map;

public class AnotherApiDemo implements AnotherApi {
    BaseUrlProvider baseUrlProvider;
    Map<Integer, Class<? extends Throwable>> addStatusExceptionMappings;

    @Override
    public int add(@RequestParam("offset") int offset) {
        LinkedHashMap pathVariables = new LinkedHashMap();
        LinkedHashMap requestParams = new LinkedHashMap();
        requestParams.put("offset", offset);
        try {
            return Integer.valueOf(UniRests.post(addStatusExceptionMappings,
                    AnotherApi.class, baseUrlProvider, "url", pathVariables, requestParams));
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
