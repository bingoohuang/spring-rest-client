package com.github.bingoohuang.springrestclient.utils;

import com.alibaba.fastjson.JSON;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

import java.util.Map;

public class UniRestUtils {
    public static <T> T asJson(String url,
                               Map<String, String> routeParams,
                               Map<String, Object> requestParams,
                               Class<T> clazz) {
        String baseUrl = "http://localhost:4849";
        GetRequest get = Unirest.get(baseUrl + url);

        for (Map.Entry<String, String> entry : routeParams.entrySet()) {
            get.routeParam(entry.getKey(), entry.getValue());
        }

        get.queryString(requestParams);

        try {
            HttpResponse<String> response = get.asString();
            String json = response.getBody();
            return JSON.parseObject(json, clazz);
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String asPrimitive(String url,
                               Map<String, String> routeParams,
                               Map<String, Object> requestParams, T bean) {
        String baseUrl = "http://localhost:4849";
        HttpRequestWithBody post = Unirest.post(baseUrl + url);

        for (Map.Entry<String, String> entry : routeParams.entrySet()) {
            post.routeParam(entry.getKey(), entry.getValue());
        }

        String json = JSON.toJSONString(bean);
        post.queryString(requestParams);

        try {
            post.header("Content-Type", "application/json;charset=UTF-8")
                    .body(json);

            HttpResponse<String> response = post.asString();
            return response.getBody();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }
}
