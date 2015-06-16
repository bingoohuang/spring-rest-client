package com.github.bingoohuang.springrestclient.provider;

import com.mashape.unirest.request.HttpRequest;

import java.util.Map;

public interface SignProvider {
    void sign(Class<?> apiClass, String uuid, Map<String, Object> requestParams, HttpRequest httpRequest);
}
