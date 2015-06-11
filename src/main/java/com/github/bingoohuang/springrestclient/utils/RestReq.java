package com.github.bingoohuang.springrestclient.utils;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.springrestclient.annotations.SuccInResponseJSONProperty;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RestReq {
    final SuccInResponseJSONProperty succInResponseJSONProperty;
    final Map<String, Object> requestParamValues;
    final Map<Integer, Class<? extends Throwable>> sendStatusExceptionMappings;
    final Class<?> apiClass;
    final BaseUrlProvider baseUrlProvider;
    final String prefix;
    final Map<String, Object> routeParams;
    final Map<String, Object> requestParams;
    private final RestLog restLog;

    RestReq(SuccInResponseJSONProperty succInResponseJSONProperty,
            Map<String, Object> requestParamValues,
            Map<Integer, Class<? extends Throwable>> sendStatusExceptionMappings,
            Class<?> apiClass,
            BaseUrlProvider baseUrlProvider,
            String prefix,
            Map<String, Object> routeParams,
            Map<String, Object> requestParams,
            boolean async) {
        this.succInResponseJSONProperty = succInResponseJSONProperty;
        this.requestParamValues = requestParamValues;
        this.sendStatusExceptionMappings = sendStatusExceptionMappings;
        this.apiClass = apiClass;
        this.baseUrlProvider = baseUrlProvider;
        this.prefix = prefix;
        this.routeParams = routeParams;
        this.requestParams = requestParams;
        this.restLog = new RestLog(apiClass, async);
    }

    static ThreadLocal<HttpResponse<String>> lastResponseTL;

    static {
        lastResponseTL = new ThreadLocal<HttpResponse<String>>();
    }

    public static HttpResponse<String> lastResponse() {
        return lastResponseTL.get();
    }

    public String get() throws Throwable {
        String url = createUrl();
        HttpRequest get = Unirest.get(url);

        get.queryString(mergeRequestParams());

        return request(get);
    }

    public Future<HttpResponse<String>> getAsync() throws Throwable {
        String url = createUrl();
        HttpRequest get = Unirest.get(url);

        get.queryString(mergeRequestParams());

        return requestAsync(get);
    }

    public String post() throws Throwable {
        String url = createUrl();
        HttpRequestWithBody post = Unirest.post(url);

        post.fields(mergeRequestParams());

        return request(post);
    }

    public Future<HttpResponse<String>> postAsync() throws Throwable {
        String url = createUrl();
        HttpRequestWithBody post = Unirest.post(url);

        post.fields(mergeRequestParams());

        return requestAsync(post);
    }

    public String postAsJson(Object bean) throws Throwable {
        String url = createUrl();
        HttpRequestWithBody post = Unirest.post(url);

        post.queryString(mergeRequestParams());

        post.header("Content-Type", "application/json;charset=UTF-8");
        post.body(JSON.toJSONString(bean));

        return request(post);
    }

    public Future<HttpResponse<String>> postAsJsonAsync(Object bean) throws Throwable {
        String url = createUrl();
        HttpRequestWithBody post = Unirest.post(url);

        post.queryString(mergeRequestParams());

        post.header("Content-Type", "application/json;charset=UTF-8");
        post.body(JSON.toJSONString(bean));

        return requestAsync(post);
    }

    private Map<String, Object> mergeRequestParams() {
        Map<String, Object> mergedRequestParams = Maps.newHashMap(requestParamValues);
        mergedRequestParams.putAll(requestParams);
        return mergedRequestParams;
    }

    private String request(HttpRequest httpRequest) throws Throwable {
        setRouteParams(httpRequest);

        boolean loggedResponse = false;
        try {
            restLog.log(httpRequest);
            lastResponseTL.remove();
            HttpResponse<String> response = httpRequest.asString();
            restLog.log(response);
            loggedResponse = true;
            lastResponseTL.set(response);

            if (isSuccessful(response)) return nullOrBody(response);

            throw processStatusExceptionMappings(response);
        } catch (UnirestException e) {
            if (!loggedResponse) restLog.log(e);
            throw new RuntimeException(e);
        } catch (Throwable e) {
            if (!loggedResponse) restLog.log(e);
            throw e;
        }
    }

    private Future<HttpResponse<String>> requestAsync(HttpRequest httpRequest) throws Throwable {
        setRouteParams(httpRequest);

        restLog.log(httpRequest);
        final long start = System.currentTimeMillis();
        lastResponseTL.remove(); // clear response threadlocal before execution
        final UniRestCallback callback = new UniRestCallback(apiClass, restLog);

        final Future<HttpResponse<String>> future = httpRequest.asStringAsync(callback);

        return new Future<HttpResponse<String>>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return future.cancel(mayInterruptIfRunning);
            }

            @Override
            public boolean isCancelled() {
                return callback.isCancelled();
            }

            @Override
            public boolean isDone() {
                return callback.isDone();
            }

            @Override
            public HttpResponse<String> get() throws InterruptedException, ExecutionException {
                return callback.get();
            }

            @Override
            public HttpResponse<String> get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return callback.get(unit.toMillis(timeout));
            }
        };
    }

    private void setRouteParams(HttpRequest httpRequest) {
        for (Map.Entry<String, Object> entry : routeParams.entrySet()) {
            httpRequest.routeParam(entry.getKey(), String.valueOf(entry.getValue()));
        }
    }

    public Throwable processStatusExceptionMappings(HttpResponse<String> response) throws Throwable {
        Class<? extends Throwable> exceptionClass = sendStatusExceptionMappings.get(response.getStatus());
        String msg = response.getHeaders().getFirst("error-msg");
        if (Strings.isNullOrEmpty(msg)) msg = response.getBody();

        if (exceptionClass == null) throw new RestException(response.getStatus(), msg);

        throw Obj.createObject(exceptionClass, msg);
    }

    private String createUrl() {
        String baseUrl = baseUrlProvider.getBaseUrl(apiClass);
        if (Strings.isNullOrEmpty(baseUrl)) {
            throw new RuntimeException("base url cannot be null generated by provider " + baseUrlProvider.getClass());
        }
        return baseUrl + prefix;
    }

    public static String nullOrBody(HttpResponse<String> response) {
        return "true".equals(response.getHeaders().getFirst("returnNull")) ? null : response.getBody();
    }

    public boolean isSuccessful(HttpResponse<String> response) {
        int status = response.getStatus();
        boolean isHttpSucc = status >= 200 && status < 300;
        if (!isHttpSucc) return false;

        if (succInResponseJSONProperty == null) return true;
        if (!isResponseJsonContentType(response)) return true;

        Map<String, Object> map = Json.unJson(response.getBody());
        String key = succInResponseJSONProperty.key();
        Object realValue = map.get(key);
        String expectedValue = succInResponseJSONProperty.value();
        return expectedValue.equals("" + realValue);
    }

    private static boolean isResponseJsonContentType(HttpResponse<String> response) {
        String contentType = response.getHeaders().getFirst("Content-Type");
        return contentType != null && contentType.contains("application/json");
    }
}
