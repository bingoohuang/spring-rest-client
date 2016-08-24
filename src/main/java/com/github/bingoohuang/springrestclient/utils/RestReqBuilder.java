package com.github.bingoohuang.springrestclient.utils;

import com.github.bingoohuang.springrestclient.annotations.SuccInResponseJSONProperty;
import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;
import com.github.bingoohuang.springrestclient.provider.SignProvider;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public class RestReqBuilder {
    SuccInResponseJSONProperty succInResponseJSONProperty;
    Map<String, Object> fixedRequestParams;
    Map<Integer, Class<? extends Throwable>> statusExceptionMapping;
    Class<?> apiClass;
    BaseUrlProvider baseUrlProvider;
    String prefix;
    Map<String, Object> routeParams;
    Map<String, Object> requestParams;
    Map<String, Object> cookies;
    boolean async;
    SignProvider signProvider;
    ApplicationContext appContext;
    String firstConsume; // consumes = {"application/xml"}

    public RestReqBuilder firstConsume(String firstConsume) {
        this.firstConsume = firstConsume;
        return this;
    }


    public RestReqBuilder appContext(ApplicationContext appContext) {
        this.appContext = appContext;
        return this;
    }

    public RestReqBuilder signProvider(SignProvider signProvider) {
        this.signProvider = signProvider;
        return this;
    }

    public RestReqBuilder async(boolean async) {
        this.async = async;
        return this;
    }

    public RestReqBuilder prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public RestReqBuilder fixedRequestParams(Map<String, Object> fixedRequestParams) {
        this.fixedRequestParams = fixedRequestParams;
        return this;
    }

    public RestReqBuilder statusExceptionMappings(Map<Integer, Class<? extends Throwable>> statusExceptionMapping) {
        this.statusExceptionMapping = statusExceptionMapping;
        return this;
    }

    public RestReqBuilder apiClass(Class<?> apiClass) {
        this.apiClass = apiClass;
        return this;
    }

    public RestReqBuilder baseUrlProvider(BaseUrlProvider baseUrlProvider) {
        this.baseUrlProvider = baseUrlProvider;
        return this;
    }

    public RestReqBuilder requestParams(Map<String, Object> requestParams) {
        this.requestParams = requestParams;
        return this;
    }

    public RestReqBuilder routeParams(Map<String, Object> routeParams) {
        this.routeParams = routeParams;
        return this;
    }

    public RestReqBuilder cookies(Map<String, Object> cookies) {
        this.cookies = cookies;
        return this;
    }

    public RestReqBuilder succInResponseJSONProperty(SuccInResponseJSONProperty succInResponseJSONProperty) {
        this.succInResponseJSONProperty = succInResponseJSONProperty;
        return this;
    }

    public RestReq build() {
        return new RestReq(
                firstConsume,
                succInResponseJSONProperty,
                fixedRequestParams,
                statusExceptionMapping,
                apiClass,
                baseUrlProvider,
                prefix,
                routeParams,
                requestParams,
                cookies,
                async,
                signProvider,
                appContext);
    }
}
