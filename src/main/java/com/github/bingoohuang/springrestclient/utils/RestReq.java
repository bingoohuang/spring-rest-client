package com.github.bingoohuang.springrestclient.utils;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.springrestclient.annotations.SuccInResponseJSONProperty;
import com.github.bingoohuang.springrestclient.exception.RestException;
import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;
import com.github.bingoohuang.springrestclient.provider.SignProvider;
import com.github.bingoohuang.utils.codec.Json;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.MultipartBody;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RestReq {
    final SuccInResponseJSONProperty succInResponseJSONProperty;
    final Map<String, Object> fixedRequestParams;
    final Map<Integer, Class<? extends Throwable>> sendStatusExceptionMappings;
    final Class<?> apiClass;
    final BaseUrlProvider baseUrlProvider;
    final String prefix;
    final Map<String, Object> routeParams;
    final Map<String, Object> requestParams;
    final RestLog restLog;
    final SignProvider signProvider;

    RestReq(SuccInResponseJSONProperty succInResponseJSONProperty,
            Map<String, Object> fixedRequestParams,
            Map<Integer, Class<? extends Throwable>> sendStatusExceptionMappings,
            Class<?> apiClass,
            BaseUrlProvider baseUrlProvider,
            String prefix,
            Map<String, Object> routeParams,
            Map<String, Object> requestParams,
            boolean async,
            SignProvider signProvider) {
        this.succInResponseJSONProperty = succInResponseJSONProperty;
        this.fixedRequestParams = fixedRequestParams;
        this.sendStatusExceptionMappings = sendStatusExceptionMappings;
        this.apiClass = apiClass;
        this.baseUrlProvider = baseUrlProvider;
        this.prefix = prefix;
        this.routeParams = routeParams;
        this.requestParams = requestParams;
        this.restLog = new RestLog(apiClass, async);
        this.signProvider = signProvider;
    }

    static ThreadLocal<HttpResponse<?>> lastResponseTL;

    static {
        lastResponseTL = new ThreadLocal<HttpResponse<?>>();
    }

    public static HttpResponse<?> lastResponse() {
        return lastResponseTL.get();
    }

    public String get() throws Throwable {
        String url = createUrl();
        HttpRequest get = Unirest.get(url);
        setRouteParams(get);

        get.queryString(mergeRequestParams());

        return request(null, get);
    }

    public InputStream getBinary() throws Throwable {
        String url = createUrl();
        HttpRequest get = Unirest.get(url);
        setRouteParams(get);

        get.queryString(mergeRequestParams());

        return requestBinary(null, get);
    }

    public Future<HttpResponse<String>> getAsync() throws Throwable {
        String url = createUrl();
        HttpRequest get = Unirest.get(url);
        setRouteParams(get);

        get.queryString(mergeRequestParams());

        return requestAsync(null, get);
    }

    public String post() throws Throwable {
        String url = createUrl();
        HttpRequestWithBody post = Unirest.post(url);
        setRouteParams(post);

        Map<String, Object> requestParams = mergeRequestParams();
        BaseRequest fields = fields(post, requestParams);

        return request(requestParams, fields);
    }

    public InputStream postBinary() throws Throwable {
        String url = createUrl();
        HttpRequestWithBody post = Unirest.post(url);
        setRouteParams(post);

        Map<String, Object> requestParams = mergeRequestParams();
        BaseRequest fields = fields(post, requestParams);

        return requestBinary(requestParams, fields);
    }


    private BaseRequest fields(HttpRequestWithBody post, Map<String, Object> requestParams) {
        MultipartBody field = null;

        for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Collection) {
                for (Object o : (Collection) value) {
                    field = fieldFileOrElse(post, field, entry, o);
                }
            } else {
                field = fieldFileOrElse(post, field, entry, value);
            }
        }

        return field == null ? post : field;
    }

    private MultipartBody fieldFileOrElse(HttpRequestWithBody post,
                                          MultipartBody field,
                                          Map.Entry<String, Object> entry,
                                          Object value) {
        if (value instanceof File) {
            if (field != null) field.field(entry.getKey(), (File) value);
            else field = post.field(entry.getKey(), (File) value);
        } else if (value instanceof org.springframework.web.multipart.MultipartFile) {
            org.springframework.web.multipart.MultipartFile file;
            file = (org.springframework.web.multipart.MultipartFile) value;
            if (field != null) field.field(entry.getKey(), file);
            else field = post.field(entry.getKey(), file);
        } else {
            if (field != null) field.field(entry.getKey(), value);
            else field = post.field(entry.getKey(), value);
        }

        return field;
    }

    public Future<HttpResponse<String>> postAsync() throws Throwable {
        String url = createUrl();
        HttpRequestWithBody post = Unirest.post(url);

        Map<String, Object> requestParams = mergeRequestParams();
        BaseRequest fields = fields(post, requestParams);

        return requestAsync(requestParams, fields);
    }

    private void setRouteParams(HttpRequest httpRequest) {
        for (Map.Entry<String, Object> entry : routeParams.entrySet()) {
            httpRequest.routeParam(entry.getKey(), String.valueOf(entry.getValue()));
        }
    }

    public String postAsJson(Object bean) throws Throwable {
        String url = createUrl();
        HttpRequestWithBody post = Unirest.post(url);
        setRouteParams(post);

        post.queryString(mergeRequestParams());

        post.header("Content-Type", "application/json;charset=UTF-8");
        String body = JSON.toJSONString(bean);
        post.body(body);

        Map<String, Object> requestParams = Maps.newHashMap();
        requestParams.put("_json", body);

        return request(requestParams, post);
    }

    public InputStream postAsJsonBinary(Object bean) throws Throwable {
        String url = createUrl();
        HttpRequestWithBody post = Unirest.post(url);
        setRouteParams(post);

        post.queryString(mergeRequestParams());

        post.header("Content-Type", "application/json;charset=UTF-8");
        String body = JSON.toJSONString(bean);
        post.body(body);

        Map<String, Object> requestParams = Maps.newHashMap();
        requestParams.put("_json", body);

        return requestBinary(requestParams, post);
    }

    public Future<HttpResponse<String>> postAsJsonAsync(Object bean) throws Throwable {
        String url = createUrl();
        HttpRequestWithBody post = Unirest.post(url);
        setRouteParams(post);
        post.queryString(mergeRequestParams());

        post.header("Content-Type", "application/json;charset=UTF-8");
        String body = JSON.toJSONString(bean);
        post.body(body);

        Map<String, Object> requestParams = Maps.newHashMap();
        requestParams.put("_json", body);

        return requestAsync(requestParams, post);
    }

    private Map<String, Object> mergeRequestParams() {
        Map<String, Object> mergedRequestParams = Maps.newHashMap(fixedRequestParams);
        mergedRequestParams.putAll(requestParams);
        return mergedRequestParams;
    }

    private String request(Map<String, Object> requestParams, BaseRequest httpRequest) throws Throwable {
        boolean loggedResponse = false;
        try {
            restLog.logAndSign(signProvider, requestParams, httpRequest.getHttpRequest());
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

    private InputStream requestBinary(Map<String, Object> requestParams, BaseRequest httpRequest) throws Throwable {
        boolean loggedResponse = false;
        try {
            restLog.logAndSign(signProvider, requestParams, httpRequest.getHttpRequest());
            lastResponseTL.remove();
            HttpResponse<InputStream> response = httpRequest.asBinary();
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

    private Future<HttpResponse<String>> requestAsync(Map<String, Object> requestParams, BaseRequest httpRequest) throws Throwable {
        restLog.logAndSign(signProvider, requestParams, httpRequest.getHttpRequest());
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

    public Throwable processStatusExceptionMappings(HttpResponse<?> response) throws Throwable {
        Class<? extends Throwable> exceptionClass = sendStatusExceptionMappings.get(response.getStatus());
        String msg = response.header("error-msg");
        if (Strings.isNullOrEmpty(msg)) {
            Object body = response.getBody();
            msg = body instanceof InputStream ? "" : ("" + body);
        }

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

    public static <T> T nullOrBody(HttpResponse<T> response) {
        return "true".equals(response.header("returnNull")) ? null : response.getBody();
    }

    public boolean isSuccessful(HttpResponse<?> response) {
        int status = response.getStatus();
        boolean isHttpSucc = status >= 200 && status < 300;
        if (!isHttpSucc) return false;

        if (succInResponseJSONProperty == null) return true;
        if (!isResponseJsonContentType(response)) return true;

        Object body = response.getBody();
        if (body instanceof InputStream) return true;

        Map<String, Object> map = Json.unJson("" + body);
        String key = succInResponseJSONProperty.key();
        Object realValue = map.get(key);
        String expectedValue = succInResponseJSONProperty.value();
        return expectedValue.equals("" + realValue);
    }

    private static boolean isResponseJsonContentType(HttpResponse<?> response) {
        String contentType = response.header("Content-Type");
        return contentType != null && contentType.contains("application/json");
    }
}
