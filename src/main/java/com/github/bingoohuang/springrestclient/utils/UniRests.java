package com.github.bingoohuang.springrestclient.utils;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.springrestclient.annotations.CheckResponseOKByJSONProperty;
import com.github.bingoohuang.springrestclient.exception.RestException;
import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;
import com.github.bingoohuang.utils.codec.Json;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
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

    public static HttpResponse<String> lastResponse() {
        return lastResponseTL.get();
    }

    public static String get(
            CheckResponseOKByJSONProperty checkResponseOKByJSONProperty,
            Map<String, Object> requestParamValues,
            Map<Integer, Class<? extends Throwable>> mappings,
            Class<?> apiClass,
            BaseUrlProvider baseUrlProvider,
            String prefix,
            Map<String, Object> routeParams,
            Map<String, Object> requestParams) throws Throwable {
        String url = createUrl(apiClass, baseUrlProvider, prefix);
        HttpRequest get = Unirest.get(url);

        get.queryString(mergeRequestParams(requestParamValues, requestParams));

        return request(checkResponseOKByJSONProperty, apiClass, mappings, routeParams, get);
    }

    public static String post(
            CheckResponseOKByJSONProperty checkResponseOKByJSONProperty,
            Map<String, Object> requestParamValues,
            Map<Integer, Class<? extends Throwable>> mappings,
            Class<?> apiClass,
            BaseUrlProvider baseUrlProvider,
            String prefix,
            Map<String, Object> routeParams,
            Map<String, Object> requestParams) throws Throwable {
        String url = createUrl(apiClass, baseUrlProvider, prefix);
        HttpRequestWithBody post = Unirest.post(url);

        post.fields(mergeRequestParams(requestParamValues, requestParams));

        return request(checkResponseOKByJSONProperty, apiClass, mappings, routeParams, post);
    }

    public static String postAsJson(
            CheckResponseOKByJSONProperty checkResponseOKByJSONProperty,
            Map<String, Object> requestParamValues,
            Map<Integer, Class<? extends Throwable>> mappings,
            Class<?> apiClass,
            BaseUrlProvider baseUrlProvider,
            String prefix,
            Map<String, Object> routeParams,
            Map<String, Object> requestParams,
            Object bean) throws Throwable {
        String url = createUrl(apiClass, baseUrlProvider, prefix);
        HttpRequestWithBody post = Unirest.post(url);

        post.queryString(mergeRequestParams(requestParamValues, requestParams));

        post.header("Content-Type", "application/json;charset=UTF-8");
        post.body(JSON.toJSONString(bean));

        return request(checkResponseOKByJSONProperty, apiClass, mappings, routeParams, post);
    }

    private static Map<String, Object> mergeRequestParams(Map<String, Object> requestParamValues,
                                                          Map<String, Object> requestParams) {
        Map<String, Object> mergedRequestParams = Maps.newHashMap(requestParamValues);
        mergedRequestParams.putAll(requestParams);
        return mergedRequestParams;
    }

    private static String request(CheckResponseOKByJSONProperty checkResponseOKByJSONProperty,
                                  Class<?> apiClass, Map<Integer, Class<? extends Throwable>> mappings,
                                  Map<String, Object> routeParams,
                                  HttpRequest httpRequest) throws Throwable {
        setRouteParams(routeParams, httpRequest);

        try {
            RequestLog.log(apiClass, httpRequest);
            long start = System.currentTimeMillis();

            HttpResponse<String> response = httpRequest.asString();
            RequestLog.log(apiClass, response, System.currentTimeMillis() - start);

            lastResponseTL.set(response);

            if (isSuccessful(checkResponseOKByJSONProperty, response)) return nullOrBody(response);

            throw processStatusExceptionMappings(response, mappings);
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setRouteParams(Map<String, Object> routeParams, HttpRequest httpRequest) {
        for (Map.Entry<String, Object> entry : routeParams.entrySet()) {
            httpRequest.routeParam(entry.getKey(), String.valueOf(entry.getValue()));
        }
    }

    private static Throwable processStatusExceptionMappings(
            HttpResponse<String> response,
            Map<Integer, Class<? extends Throwable>> mappings)
            throws Throwable {
        Class<? extends Throwable> exceptionClass = mappings.get(response.getStatus());
        String msg = response.getHeaders().getFirst("error-msg");
        if (Strings.isNullOrEmpty(msg)) msg = response.getBody();

        if (exceptionClass == null) throw new RestException(response.getStatus(), msg);

        throw Obj.createObject(exceptionClass, msg);
    }

    private static String createUrl(Class<?> apiClass,
                                    BaseUrlProvider baseUrlProvider, String prefix) {
        String baseUrl = baseUrlProvider.getBaseUrl(apiClass);
        if (Strings.isNullOrEmpty(baseUrl)) {
            throw new RuntimeException("base url cannot be null generated by provider " + baseUrlProvider.getClass());
        }
        return baseUrl + prefix;
    }

    private static String nullOrBody(HttpResponse<String> response) {
        return "true".equals(response.getHeaders().getFirst("returnNull")) ? null : response.getBody();
    }

    private static boolean isSuccessful(CheckResponseOKByJSONProperty checkResponseOKByJSONProperty,
                                        HttpResponse<String> response) {
        int status = response.getStatus();
        boolean isHttpSucc = status >= 200 && status < 300;
        if (!isHttpSucc) return false;

        if (checkResponseOKByJSONProperty == null) return true;
        if (!isResponseJsonContentType(response)) return true;

        Map<String, Object> map = Json.unJson(response.getBody());
        String key = checkResponseOKByJSONProperty.key();
        Object realValue = map.get(key);
        String expectedValue = checkResponseOKByJSONProperty.value();
        return expectedValue.equals("" + realValue);
    }

    private static boolean isResponseJsonContentType(HttpResponse<String> response) {
        String contentType = response.getHeaders().getFirst("Content-Type");
        return contentType != null && contentType.contains("application/json");
    }

}
