package com.github.bingoohuang.springrestclient.utils;

import com.github.bingoohuang.springrestclient.ext.ParameterDelayable;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.experimental.var;
import lombok.val;
import org.springframework.context.ApplicationContext;

import java.util.Map;

@AllArgsConstructor
public class RequestParamsHelper {
    final Map<String, Object> fixedRequestParams;
    final Map<String, Object> requestParams;
    final ApplicationContext appContext;

    private boolean isQueryParam(String name) {
        return name.startsWith("q^");
    }

    private String parseQueryRealName(String name) {
        return name.substring(2);
    }

    Map<String, Object> createQueryParamsForPost() {
        Map<String, Object> queryParams = Maps.newHashMap();
        Map<String, ParameterDelayable> delayedParameters = Maps.newLinkedHashMap();

        for (val entry : fixedRequestParams.entrySet()) {
            val name = entry.getKey();
            if (!isQueryParam(name)) continue;

            val beanValue = createFixedRequestParamValue(entry);
            val realName = parseQueryRealName(name);

            if (beanValue instanceof ParameterDelayable) {
                delayedParameters.put(realName, (ParameterDelayable) beanValue);
            } else {
                queryParams.put(realName, beanValue);
            }
        }

        for (val entry : requestParams.entrySet()) {
            val name = entry.getKey();
            if (!isQueryParam(name)) continue;

            val beanValue = entry.getValue();
            val realName = parseQueryRealName(name);
            queryParams.put(realName, beanValue);
        }

        computeDelayedParameters(queryParams, delayedParameters);

        return queryParams;
    }

    Map<String, Object> mergeRequestParamsForGet() {
        Map<String, Object> mergedRequestParams = Maps.newHashMap();
        Map<String, ParameterDelayable> delayedParameters = Maps.newLinkedHashMap();

        for (val entry : fixedRequestParams.entrySet()) {
            var name = entry.getKey();
            val beanValue = createFixedRequestParamValue(entry);
            if (isQueryParam(name)) name = parseQueryRealName(name);

            if (beanValue instanceof ParameterDelayable) {
                delayedParameters.put(name, (ParameterDelayable) beanValue);
            } else {
                mergedRequestParams.put(name, beanValue);
            }
        }

        for (val entry : requestParams.entrySet()) {
            var name = entry.getKey();
            if (isQueryParam(name)) name = parseQueryRealName(name);
            mergedRequestParams.put(name, entry.getValue());
        }

        computeDelayedParameters(mergedRequestParams, delayedParameters);

        return mergedRequestParams;
    }


    Map<String, Object> mergeRequestParamsWithoutQueryParams() {
        Map<String, Object> mergedRequestParams = Maps.newHashMap();
        Map<String, ParameterDelayable> delayedParameters = Maps.newLinkedHashMap();

        for (val entry : fixedRequestParams.entrySet()) {
            var name = entry.getKey();
            val beanValue = createFixedRequestParamValue(entry);
            if (isQueryParam(name)) continue;

            if (beanValue instanceof ParameterDelayable) {
                delayedParameters.put(name, (ParameterDelayable) beanValue);
            } else {
                mergedRequestParams.put(name, beanValue);
            }
        }

        for (val entry : requestParams.entrySet()) {
            String name = entry.getKey();
            if (isQueryParam(name)) continue;

            mergedRequestParams.put(name, entry.getValue());
        }

        computeDelayedParameters(mergedRequestParams, delayedParameters);

        return mergedRequestParams;
    }

    private void computeDelayedParameters(Map<String, Object> mergedRequestParams,
                                          Map<String, ParameterDelayable> delayedParams) {
        for (val entry : delayedParams.entrySet()) {
            val value = entry.getValue().computeDelayedParam(mergedRequestParams);
            mergedRequestParams.put(entry.getKey(), value);
        }
    }


    private Object createFixedRequestParamValue(Map.Entry<String, Object> entry) {
        val value = entry.getValue();
        if (value instanceof Class && value != void.class) {
            val bean = Obj.getOrCreateBean(appContext, (Class) value);

            return bean instanceof ParameterDelayable ? bean : bean.toString();
        }

        return value;
    }

}
