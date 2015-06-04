package com.github.bingoohuang.springrestclient.utils;

import com.alibaba.fastjson.JSON;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

public class UniRestUtils {
    public static String get(String url,
                             Map<String, String> routeParams,
                             Map<String, Object> requestParams) {
        String baseUrl = "http://localhost:4849";
        HttpRequest get = Unirest.get(baseUrl + url);

        for (Map.Entry<String, String> entry : routeParams.entrySet()) {
            get.routeParam(entry.getKey(), entry.getValue());
        }

        get.queryString(requestParams);

        try {
            HttpResponse<String> response = get.asString();
            return response.getBody();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    public static String post(String url,
                              Map<String, String> routeParams,
                              Map<String, Object> requestParams) {
        String baseUrl = "http://localhost:4849";
        HttpRequestWithBody post = Unirest.post(baseUrl + url);

        for (Map.Entry<String, String> entry : routeParams.entrySet()) {
            post.routeParam(entry.getKey(), entry.getValue());
        }

        post.queryString(requestParams);

        try {
            post.header("Content-Type", "application/json;charset=UTF-8");

            HttpResponse<String> response = post.asString();
            if (response.getStatus() >= 200 && response.getStatus() < 300)
                return response.getBody();

            throw new RuntimeException();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    public static String postAsJson(String url,
                                    Map<String, String> routeParams,
                                    Map<String, Object> requestParams,
                                    Object bean) {
        String baseUrl = "http://localhost:4849";
        HttpRequestWithBody post = Unirest.post(baseUrl + url);

        for (Map.Entry<String, String> entry : routeParams.entrySet()) {
            post.routeParam(entry.getKey(), entry.getValue());
        }

        String json = JSON.toJSONString(bean);
        post.queryString(requestParams);

        try {
            post.header("Content-Type", "application/json;charset=UTF-8");
            post.body(json);

            HttpResponse<String> response = post.asString();
            if (response.getStatus() >= 200 && response.getStatus() < 300)
                return response.getBody();

            throw new RuntimeException();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }


}
