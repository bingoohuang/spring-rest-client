package com.github.bingoohuang.springrestclient.boot.resolver;

import com.github.bingoohuang.utils.codec.Json;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class HiRequestParamMethodArgumentResolver extends RequestParamMethodArgumentResolver {
    public HiRequestParamMethodArgumentResolver(boolean useDefaultResolution) {
        super(useDefaultResolution);
    }

    public HiRequestParamMethodArgumentResolver(ConfigurableBeanFactory beanFactory, boolean useDefaultResolution) {
        super(beanFactory, useDefaultResolution);
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest webRequest) throws Exception {
        Object arg = super.resolveName(name, parameter, webRequest);
        if (arg instanceof String) {
            try {
                Object obj = tryUnJson(parameter, (String) arg);
                if (obj != null) return obj;
            } catch (Throwable e) {
                // ignore
            }
        }

        return arg;
    }

    private Object tryUnJson(MethodParameter parameter, String str) {
        char c = str.charAt(0);
        boolean maybeJson = c == '{';
        boolean maybeJsonArray = c == '[';
        Object obj = null;
        if (maybeJson) {
            obj = Json.unJson(str, parameter.getParameterType());
        } else if (maybeJsonArray) {
            Type genericParameterType = parameter.getGenericParameterType();
            boolean isGeneric = genericParameterType instanceof ParameterizedType;
            if (isGeneric) {
                ParameterizedType parameterizedType = (ParameterizedType) genericParameterType;
                Class<?> argType = (Class) parameterizedType.getActualTypeArguments()[0];

                obj = Json.unJsonArray(str, argType);
            }
        }
        return obj;
    }
}
