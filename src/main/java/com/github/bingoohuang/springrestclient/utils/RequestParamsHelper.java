package com.github.bingoohuang.springrestclient.utils;

import com.google.common.collect.Maps;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public class RequestParamsHelper {
    final Map<String, Object> fixedRequestParams;
    final Map<String, Object> requestParams;
    final ApplicationContext appContext;

    public RequestParamsHelper(Map<String, Object> fixedRequestParams, Map<String, Object> requestParams, ApplicationContext appContext) {
        this.fixedRequestParams = fixedRequestParams;
        this.requestParams = requestParams;
        this.appContext = appContext;
    }

    private boolean isQueryParam(String name) {
        return name.startsWith("q^");
    }

    private String parseQueryRealName(String name) {
        return name.substring(2);
    }

    Map<String, Object> createQueryParams() {
        Map<String, Object> queryParams = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : fixedRequestParams.entrySet()) {
            String name = entry.getKey();
            if (isQueryParam(name)) {
                Object value = createFixedRequestParamValue(entry);
                queryParams.put(parseQueryRealName(name), value);
            }
        }

        for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
            String name = entry.getKey();
            if (isQueryParam(name)) {
                queryParams.put(parseQueryRealName(name), entry.getValue());
            }
        }

        return queryParams;
    }

    Map<String, Object> mergeRequestParams() {
        Map<String, Object> mergedRequestParams = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : fixedRequestParams.entrySet()) {
            String name = entry.getKey();
            Object value = createFixedRequestParamValue(entry);
            if (isQueryParam(name)) name = parseQueryRealName(name);
            mergedRequestParams.put(name, value);
        }

        for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
            String name = entry.getKey();
            if (isQueryParam(name)) name = parseQueryRealName(name);
            mergedRequestParams.put(name, entry.getValue());
        }

        return mergedRequestParams;
    }


    Map<String, Object> mergeRequestParamsWithoutQueryParams() {
        Map<String, Object> mergedRequestParams = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : fixedRequestParams.entrySet()) {
            String name = entry.getKey();
            Object value = createFixedRequestParamValue(entry);
            if (isQueryParam(name)) continue;

            mergedRequestParams.put(name, value);
        }

        for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
            String name = entry.getKey();
            if (isQueryParam(name)) continue;

            mergedRequestParams.put(name, entry.getValue());
        }

        return mergedRequestParams;
    }

    private Object createFixedRequestParamValue(Map.Entry<String, Object> entry) {
        Object value = entry.getValue();
        if (value instanceof Class && value != void.class) {
            Class requiredType = (Class) value;
            Object bean = Obj.getOrCreateBean(appContext, requiredType);
            value = bean.toString();
        }
        return value;
    }

}
