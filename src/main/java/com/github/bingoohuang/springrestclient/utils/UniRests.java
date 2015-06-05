package com.github.bingoohuang.springrestclient.utils;

import com.alibaba.fastjson.JSON;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

import java.util.Map;

public class UniRests {
    static ThreadLocal<HttpResponse<String>> lastResponseTL;

    static {
        lastResponseTL = new ThreadLocal<HttpResponse<String>>();
    }

    public static String get(String url,
                             Map<String, String> routeParams,
                             Map<String, Object> requestParams) {
        HttpRequest get = Unirest.get(url);

        for (Map.Entry<String, String> entry : routeParams.entrySet()) {
            get.routeParam(entry.getKey(), entry.getValue());
        }

        get.queryString(requestParams);

        try {
            HttpResponse<String> response = get.asString();
            lastResponseTL.set(response);

            if (isSuccessful(response)) return nullOrBody(response);

            throw new RuntimeException();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    private static String nullOrBody(HttpResponse<String> response) {
        return "true".equals(response.getHeaders().getFirst("returnNull")) ? null : response.getBody();
    }

    public static String post(String url,
                              Map<String, String> routeParams,
                              Map<String, Object> requestParams) {
        HttpRequestWithBody post = Unirest.post(url);

        for (Map.Entry<String, String> entry : routeParams.entrySet()) {
            post.routeParam(entry.getKey(), entry.getValue());
        }

        post.fields(requestParams);

        try {
            HttpResponse<String> response = post.asString();
            lastResponseTL.set(response);

            if (isSuccessful(response)) return nullOrBody(response);

            throw new RuntimeException();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isSuccessful(HttpResponse<String> response) {
        int status = response.getStatus();
        return status >= 200 && status < 300;
    }

    public static String postAsJson(String url,
                                    Map<String, String> routeParams,
                                    Map<String, Object> requestParams,
                                    Object bean) {
        HttpRequestWithBody post = Unirest.post(url);

        for (Map.Entry<String, String> entry : routeParams.entrySet()) {
            post.routeParam(entry.getKey(), entry.getValue());
        }

        post.queryString(requestParams);

        try {
            post.header("Content-Type", "application/json;charset=UTF-8");
            post.body(JSON.toJSONString(bean));

            HttpResponse<String> response = post.asString();
            lastResponseTL.set(response);

            if (isSuccessful(response)) return nullOrBody(response);

            throw new RuntimeException();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpResponse<String> lastResponse() {
        return lastResponseTL.get();
    }
}
