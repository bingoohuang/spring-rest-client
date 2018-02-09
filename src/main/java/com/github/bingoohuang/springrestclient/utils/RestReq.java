package com.github.bingoohuang.springrestclient.utils;

import com.github.bingoohuang.springrestclient.annotations.SuccInResponseJSONProperty;
import com.github.bingoohuang.springrestclient.exception.RestException;
import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;
import com.github.bingoohuang.springrestclient.provider.BasicAuthProvider;
import com.github.bingoohuang.springrestclient.provider.SignProvider;
import com.github.bingoohuang.springrestclient.xml.Xmls;
import com.github.bingoohuang.utils.codec.Json;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.ValueUtils;
import com.mashape.unirest.request.body.MultipartBody;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class RestReq {
    final SuccInResponseJSONProperty succInResponseJSONProperty;
    final Map<String, Object> fixedRequestParams;
    final Map<Integer, Class<? extends Throwable>> sendStatusExceptionMappings;
    final Class<?> apiClass;
    final BaseUrlProvider baseUrlProvider;
    final String prefix;
    final Map<String, Object> routeParams;
    final Map<String, Object> requestParams;
    final Map<String, Object> cookies;
    final RestLog restLog;
    final SignProvider signProvider;
    final ApplicationContext appContext;
    final RequestParamsHelper requestParamsHelper;
    final String firstConsume;
    final BasicAuthProvider basicAuthProvider;

    RestReq(
            BasicAuthProvider basicAuthProvider,
            String firstConsume,
            SuccInResponseJSONProperty succInResponseJSONProperty,
            Map<String, Object> fixedRequestParams,
            Map<Integer, Class<? extends Throwable>> sendStatusExceptionMappings,
            Class<?> apiClass,
            BaseUrlProvider baseUrlProvider,
            String prefix,
            Map<String, Object> routeParams,
            Map<String, Object> requestParams,
            Map<String, Object> cookies,
            boolean async,
            SignProvider signProvider,
            ApplicationContext appContext) {
        this.basicAuthProvider = basicAuthProvider;
        this.firstConsume = firstConsume;
        this.succInResponseJSONProperty = succInResponseJSONProperty;
        this.fixedRequestParams = fixedRequestParams;
        this.sendStatusExceptionMappings = sendStatusExceptionMappings;
        this.apiClass = apiClass;
        this.baseUrlProvider = baseUrlProvider;
        this.prefix = prefix;
        this.routeParams = routeParams;
        this.requestParams = requestParams;
        this.cookies = cookies;
        this.restLog = new RestLog(apiClass, async);
        this.signProvider = signProvider;
        this.appContext = appContext;

        this.requestParamsHelper = new RequestParamsHelper(
                fixedRequestParams, requestParams, appContext);
    }

    static ThreadLocal<HttpResponse<?>> lastResponseTL;

    static {
        lastResponseTL = new ThreadLocal<HttpResponse<?>>();
    }

    public static HttpResponse<?> lastResponse() {
        return lastResponseTL.get();
    }

    public String get() throws Throwable {
        val url = createUrl();
        val get = Unirest.get(url);
        setRouteParamsAndCookie(get);

        get.queryString(requestParamsHelper.mergeRequestParamsForGet());

        return request(null, get);
    }

    public InputStream getBinary() throws Throwable {
        val url = createUrl();
        val get = Unirest.get(url);
        setRouteParamsAndCookie(get);

        get.queryString(requestParamsHelper.mergeRequestParamsForGet());

        return requestBinary(null, get);
    }

    public Future<HttpResponse<String>> getAsync() {
        val url = createUrl();
        val get = Unirest.get(url);
        setRouteParamsAndCookie(get);

        get.queryString(requestParamsHelper.mergeRequestParamsForGet());

        return requestAsync(null, get);
    }

    public Future<HttpResponse<InputStream>> getAsyncBinary() {
        val url = createUrl();
        val get = Unirest.get(url);
        setRouteParamsAndCookie(get);

        get.queryString(requestParamsHelper.mergeRequestParamsForGet());

        return requestAsyncBinary(null, get);
    }

    public String post() throws Throwable {
        val url = createUrl();
        val post = Unirest.post(url);
        setRouteParamsAndCookie(post);
        post.queryString(requestParamsHelper.createQueryParamsForPost());

        val requestParams = requestParamsHelper.mergeRequestParamsWithoutQueryParams();
        BaseRequest fields = fields(post, requestParams);

        return request(requestParams, fields);
    }

    public InputStream postBinary() throws Throwable {
        val url = createUrl();
        val post = Unirest.post(url);
        setRouteParamsAndCookie(post);
        post.queryString(requestParamsHelper.createQueryParamsForPost());

        val requestParams = requestParamsHelper.mergeRequestParamsWithoutQueryParams();
        val fields = fields(post, requestParams);

        return requestBinary(requestParams, fields);
    }


    private BaseRequest fields(
            HttpRequestWithBody post, Map<String, Object> requestParams) {
        MultipartBody field = null;

        for (val entry : requestParams.entrySet()) {
            val value = entry.getValue();
            boolean isFileCollection = false;
            if (value instanceof Collection) {
                isFileCollection = true;
                for (Object o : (Collection) value) {
                    if (o instanceof File || o instanceof MultipartFile)
                        field = fieldFileOrElse(post, field, entry, o);
                    else {
                        isFileCollection = false;
                        break;
                    }
                }
            }

            if (!isFileCollection) {
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
        } else if (value instanceof MultipartFile) {
            if (field != null)
                field.field(entry.getKey(), (MultipartFile) value);
            else field = post.field(entry.getKey(), (MultipartFile) value);
        } else {
            if (field != null) field.field(entry.getKey(), value);
            else field = post.field(entry.getKey(), value);
        }

        return field;
    }

    public Future<HttpResponse<String>> postAsync() {
        val url = createUrl();
        val post = Unirest.post(url);
        setRouteParamsAndCookie(post);
        post.queryString(requestParamsHelper.createQueryParamsForPost());

        val requestParams = requestParamsHelper.mergeRequestParamsWithoutQueryParams();
        val fields = fields(post, requestParams);

        return requestAsync(requestParams, fields);
    }

    public Future<HttpResponse<InputStream>> postAsyncBinary() {
        val url = createUrl();
        val post = Unirest.post(url);
        setRouteParamsAndCookie(post);
        post.queryString(requestParamsHelper.createQueryParamsForPost());

        val requestParams = requestParamsHelper.mergeRequestParamsWithoutQueryParams();
        val fields = fields(post, requestParams);

        return requestAsyncBinary(requestParams, fields);
    }


    private void setRouteParamsAndCookie(HttpRequest httpRequest) {
        Blackcats.prepareRPC(httpRequest);

        for (val entry : routeParams.entrySet()) {
            val value = String.valueOf(entry.getValue());
            httpRequest.routeParam(entry.getKey(), value);
        }

        val cookieStr = new StringBuilder();
        for (Map.Entry<String, Object> entry : cookies.entrySet()) {
            val value = String.valueOf(entry.getValue());
            cookieStr.append(' ').append(entry.getKey()).append("=").append(value).append(";");
        }
        if (cookieStr.length() > 0)
            httpRequest.header("Cookie", cookieStr.toString());

        if (basicAuthProvider != null) {
            httpRequest.basicAuth(basicAuthProvider.username(), basicAuthProvider.password());
        }
    }

    public String postBody(Object[] bean) throws Throwable {
        val post = createPost();

        val body = createBody(post, bean);
        post.body(body);

        val requestParams = createJsonBody(body);

        return request(requestParams, post);
    }

    public InputStream postBodyBinary(Object[] bean) throws Throwable {
        val post = createPost();

        val body = createBody(post, bean);
        post.body(body);

        val requestParams = createJsonBody(body);

        return requestBinary(requestParams, post);
    }

    public Future<HttpResponse<String>> postBodyAsync(Object[] bean) {
        val post = createPost();

        val body = createBody(post, bean);
        post.body(body);

        val requestParams = createJsonBody(body);

        return requestAsync(requestParams, post);
    }

    public Future<HttpResponse<InputStream>> postBodyAsyncBinary(Object[] bean) {
        val post = createPost();

        val body = createBody(post, bean);
        post.body(body);

        val requestParams = createJsonBody(body);

        return requestAsyncBinary(requestParams, post);
    }


    private Map<String, Object> createJsonBody(String body) {
        Map<String, Object> requestParams = Maps.newHashMap();
        requestParams.put("_json", body);
        return requestParams;
    }


    private HttpRequestWithBody createPost() {
        val url = createUrl();
        val post = Unirest.post(url);
        setRouteParamsAndCookie(post);
        post.queryString(requestParamsHelper.mergeRequestParamsForGet());
        return post;
    }

    private String createBody(HttpRequestWithBody post, Object... beans) {
        if (firstConsume != null && firstConsume.indexOf("/xml") >= 0) {
            post.header("Content-Type", "application/xml;charset=UTF-8");
            return marshalBeans(beans);
        } else {
            post.header("Content-Type", "application/json;charset=UTF-8");
            return tryBeansJson(beans);
        }
    }

    public static String marshalBeans(Object... beans) {
        val jsons = new StringBuilder();
        for (val bean : beans) {
            val xml = Xmls.marshal(bean);
            if (jsons.length() == 0) {
                jsons.append(xml);
            } else {
                jsons.delete(jsons.lastIndexOf("</"), jsons.length());
                int start = xml.indexOf(">") + 1;
                jsons.append(xml.substring(start));
            }
        }

        return jsons.toString();
    }

    public static String tryBeansJson(Object... beans) {
        val jsons = new StringBuilder();
        for (val bean : beans) {
            String json = ValueUtils.processValue(bean);
            if (StringUtils.isNotBlank(json) && json.indexOf('{') == 0) {
                jsons.append(jsons.length() > 0 ? "," : "{");
                jsons.append(json.substring(1, json.length() - 1));
            }
        }
        jsons.append("}");

        return jsons.toString();
    }


    private String request(Map<String, Object> reqParams, BaseRequest httpReq)
            throws Throwable {
        boolean loggedResponse = false;
        try {
            restLog.logAndSign(signProvider, reqParams, httpReq.getHttpRequest());
            lastResponseTL.remove();
            val response = httpReq.asString();
            restLog.log(response);
            loggedResponse = true;
            lastResponseTL.set(response);

            if (isSuccessful(response))
                return RestClientUtils.nullOrBody(response);

            throw processStatusExceptionMappings(response);
        } catch (UnirestException e) {
            if (!loggedResponse) restLog.log(e);
            throw new RuntimeException(e);
        } catch (Throwable e) {
            if (!loggedResponse) restLog.log(e);
            throw e;
        }
    }

    private InputStream requestBinary(Map<String, Object> reqParams, BaseRequest httpReq)
            throws Throwable {
        boolean loggedResponse = false;
        try {
            restLog.logAndSign(signProvider, reqParams, httpReq.getHttpRequest());
            lastResponseTL.remove();
            val response = httpReq.asBinary();
            restLog.log(response);
            loggedResponse = true;
            lastResponseTL.set(response);

            if (isSuccessful(response))
                return RestClientUtils.nullOrBody(response);

            throw processStatusExceptionMappings(response);
        } catch (UnirestException e) {
            if (!loggedResponse) restLog.log(e);
            throw new RuntimeException(e);
        } catch (Throwable e) {
            if (!loggedResponse) restLog.log(e);
            throw e;
        }
    }

    private Future<HttpResponse<String>> requestAsync(
            Map<String, Object> reqParams, BaseRequest httpReq) {
        restLog.logAndSign(signProvider, reqParams, httpReq.getHttpRequest());
        lastResponseTL.remove(); // clear response threadlocal before execution
        val callback = new UniRestCallback<String>(apiClass, restLog);
        val future = httpReq.asStringAsync(callback);

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
            public HttpResponse<String> get()
                    throws InterruptedException {
                return callback.get();
            }

            @Override
            public HttpResponse<String> get(long timeout, TimeUnit unit)
                    throws InterruptedException {
                return callback.get(unit.toMillis(timeout));
            }
        };
    }

    private Future<HttpResponse<InputStream>> requestAsyncBinary(
            Map<String, Object> requestParams, BaseRequest httpRequest) {
        restLog.logAndSign(signProvider, requestParams, httpRequest.getHttpRequest());
        lastResponseTL.remove(); // clear response threadlocal before execution
        val callback = new UniRestCallback<InputStream>(apiClass, restLog);
        val future = httpRequest.asBinaryAsync(callback);

        return new Future<HttpResponse<InputStream>>() {
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
            public HttpResponse<InputStream> get()
                    throws InterruptedException {
                return callback.get();
            }

            @Override
            public HttpResponse<InputStream> get(long timeout, TimeUnit unit)
                    throws InterruptedException {
                return callback.get(unit.toMillis(timeout));
            }
        };
    }

    public Throwable processStatusExceptionMappings(HttpResponse<?> response) {
        val exceptionClass = sendStatusExceptionMappings.get(response.getStatus());
        String msg = response.header("error-msg");
        if (Strings.isNullOrEmpty(msg)) {
            Object body = response.getBody();
            msg = body instanceof InputStream ? "" : ("" + body);
        }

        if (exceptionClass == null)
            return new RestException(response.getStatus(), msg);

        return Obj.createObject(exceptionClass, msg);
    }

    private String createUrl() {
        val baseUrl = baseUrlProvider.getBaseUrl(apiClass);
        if (Strings.isNullOrEmpty(baseUrl)) {
            throw new RuntimeException(
                    "base url cannot be null generated by provider "
                            + baseUrlProvider.getClass());
        }
        return baseUrl + prefix;
    }


    public boolean isSuccessful(HttpResponse<?> response) {
        val status = response.getStatus();
        val isHttpSucc = status >= 200 && status < 300;
        if (!isHttpSucc) return false;

        if (succInResponseJSONProperty == null) return true;
        if (!RestClientUtils.isResponseJsonContentType(response)) return true;

        val body = response.getBody();
        if (body instanceof InputStream) return true;

        val map = Json.unJson("" + body);
        val key = succInResponseJSONProperty.key();
        val realValue = map.get(key);
        val expectedValue = succInResponseJSONProperty.value();
        return expectedValue.equals("" + realValue);
    }

}
