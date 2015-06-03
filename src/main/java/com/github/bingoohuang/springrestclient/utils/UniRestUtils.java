package com.github.bingoohuang.springrestclient.utils;

import com.alibaba.fastjson.JSON;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

import java.util.Map;

public class UniRestUtils {
    public static <T> T request(String url,
                                Map<String, String> routeParams,
                                Map<String, Object> requestParams,
                                Class<T> clazz) {
        String baseUrl = "http://localhost:4849";
        GetRequest request = Unirest.get(baseUrl + url);

        for (Map.Entry<String, String> entry : routeParams.entrySet()) {
            request.routeParam(entry.getKey(), entry.getValue());
        }

        request.queryString(requestParams);

        try {
            HttpResponse<String> response = request.asString();
            String json = response.getBody();
            return JSON.parseObject(json, clazz);
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }
}
