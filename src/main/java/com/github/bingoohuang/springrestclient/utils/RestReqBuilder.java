package com.github.bingoohuang.springrestclient.utils;

import com.github.bingoohuang.springrestclient.annotations.SuccInResponseJSONProperty;
import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;

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

    public RestReqBuilder succInResponseJSONProperty(SuccInResponseJSONProperty succInResponseJSONProperty) {
        this.succInResponseJSONProperty = succInResponseJSONProperty;
        return this;
    }

    public RestReq build() {
        return new RestReq(succInResponseJSONProperty,
                fixedRequestParams,
                statusExceptionMapping,
                apiClass,
                baseUrlProvider,
                prefix,
                routeParams,
                requestParams);
    }
}
