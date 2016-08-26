package com.github.bingoohuang.springrestclient.utils;

import com.github.bingoohuang.springrestclient.ext.ParameterDelayable;
import com.google.common.collect.Maps;
import lombok.val;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public class RequestParamsHelper {
    final Map<String, Object> fixedRequestParams;
    final Map<String, Object> requestParams;
    final ApplicationContext appContext;

    public RequestParamsHelper(
        Map<String, Object> fixedRequestParams,
        Map<String, Object> requestParams,
        ApplicationContext appContext) {
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

    Map<String, Object> createQueryParamsForPost() {
        Map<String, Object> queryParams = Maps.newHashMap();
        Map<String, ParameterDelayable> delayedParameters = Maps.newLinkedHashMap();

        for (Map.Entry<String, Object> entry : fixedRequestParams.entrySet()) {
            val name = entry.getKey();
            if (!isQueryParam(name)) continue;

            Object beanValue = createFixedRequestParamValue(entry);
            val realName = parseQueryRealName(name);

            if (beanValue instanceof ParameterDelayable) {
                delayedParameters.put(realName, (ParameterDelayable) beanValue);
            } else {
                queryParams.put(realName, beanValue);
            }
        }

        for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
            String name = entry.getKey();
            if (!isQueryParam(name)) continue;

            Object beanValue = entry.getValue();
            String realName = parseQueryRealName(name);
            queryParams.put(realName, beanValue);
        }

        computeDelayedParameters(queryParams, delayedParameters);

        return queryParams;
    }

    Map<String, Object> mergeRequestParamsForGet() {
        Map<String, Object> mergedRequestParams = Maps.newHashMap();
        Map<String, ParameterDelayable> delayedParameters = Maps.newLinkedHashMap();

        for (Map.Entry<String, Object> entry : fixedRequestParams.entrySet()) {
            String name = entry.getKey();
            Object beanValue = createFixedRequestParamValue(entry);
            if (isQueryParam(name)) name = parseQueryRealName(name);

            if (beanValue instanceof ParameterDelayable) {
                delayedParameters.put(name, (ParameterDelayable) beanValue);
            } else {
                mergedRequestParams.put(name, beanValue);
            }
        }

        for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
            String name = entry.getKey();
            if (isQueryParam(name)) name = parseQueryRealName(name);
            mergedRequestParams.put(name, entry.getValue());
        }

        computeDelayedParameters(mergedRequestParams, delayedParameters);

        return mergedRequestParams;
    }


    Map<String, Object> mergeRequestParamsWithoutQueryParams() {
        Map<String, Object> mergedRequestParams = Maps.newHashMap();
        Map<String, ParameterDelayable> delayedParameters = Maps.newLinkedHashMap();

        for (Map.Entry<String, Object> entry : fixedRequestParams.entrySet()) {
            String name = entry.getKey();
            Object beanValue = createFixedRequestParamValue(entry);
            if (isQueryParam(name)) continue;

            if (beanValue instanceof ParameterDelayable) {
                delayedParameters.put(name, (ParameterDelayable) beanValue);
            } else {
                mergedRequestParams.put(name, beanValue);
            }
        }

        for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
            String name = entry.getKey();
            if (isQueryParam(name)) continue;

            mergedRequestParams.put(name, entry.getValue());
        }

        computeDelayedParameters(mergedRequestParams, delayedParameters);

        return mergedRequestParams;
    }

    private void computeDelayedParameters(
        Map<String, Object> mergedRequestParams,
        Map<String, ParameterDelayable> delayedParams) {
        for (Map.Entry<String, ParameterDelayable> entry : delayedParams.entrySet()) {
            String value = entry.getValue().computeDelayedParam(mergedRequestParams);
            mergedRequestParams.put(entry.getKey(), value);
        }
    }


    private Object createFixedRequestParamValue(Map.Entry<String, Object> entry) {
        Object value = entry.getValue();
        if (value instanceof Class && value != void.class) {
            Class requiredType = (Class) value;
            Object bean = Obj.getOrCreateBean(appContext, requiredType);

            return bean instanceof ParameterDelayable ? bean : bean.toString();
        }

        return value;
    }

}
