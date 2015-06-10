package com.github.bingoohuang.springrestclient.generators;

import com.github.bingoohuang.springrestclient.annotations.*;
import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;
import com.github.bingoohuang.springrestclient.provider.FixedBaseUrlProvider;
import com.github.bingoohuang.springrestclient.provider.NoneBaseUrlProvider;
import com.github.bingoohuang.springrestclient.utils.Obj;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.UncheckedExecutionException;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SpringRestClientFactory {
    private static LoadingCache<Class, Object> restClientCache =
            CacheBuilder.newBuilder().build(new CacheLoader<Class, Object>() {
                @Override
                public Object load(Class restClientClass) throws Exception {
                    ClassGenerator generator = new ClassGenerator(restClientClass);
                    Class<?> restClientImplClass = generator.generate();
                    Object object = Obj.createObject(restClientImplClass);

                    setBaseUrlProvider(restClientImplClass, object, restClientClass);
                    setStatusMappings(restClientImplClass, object, restClientClass);
                    setFixedRequestParams(restClientImplClass, object, restClientClass);
                    setSuccInResponseJSONProperty(restClientImplClass, object, restClientClass);

                    return object;
                }
            });

    public static <T> T getRestClient(final Class<T> restClientClass) {
        Obj.ensureInterface(restClientClass);
        try {
            return (T) restClientCache.get(restClientClass);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            throw Throwables.propagate(cause);
        } catch (UncheckedExecutionException e) {
            Throwable cause = e.getCause();
            throw Throwables.propagate(cause);
        }
    }

    private static void setSuccInResponseJSONProperty(
            Class<?> restClientImplClass, Object object, Class<?> restClientClass) {
        for (Method method : restClientClass.getDeclaredMethods()) {
            SuccInResponseJSONProperty property = method.getAnnotation(SuccInResponseJSONProperty.class);
            if (property == null) property = restClientClass.getAnnotation(SuccInResponseJSONProperty.class);
            
            String fieldName = method.getName() + MethodGenerator.SuccInResponseJSONProperty;
            Obj.setField(restClientImplClass, object, fieldName, property);
        }
    }

    private static void setFixedRequestParams(Class<?> restClientImplClass, Object object, Class restClientClass) {
        for (Method method : restClientClass.getDeclaredMethods()) {
            Map<String, Object> mappings = createFixedRequestParams(method, restClientClass);
            String fieldName = method.getName() + MethodGenerator.FixedRequestParams;
            Obj.setField(restClientImplClass, object, fieldName, mappings);
        }
    }

    private static Map<String, Object> createFixedRequestParams(Method method, Class<?> restClientClass) {
        HashMap<String, Object> map = Maps.newHashMap();

        putRequestParams(map, restClientClass.getAnnotation(FixedRequestParam.class),
                restClientClass.getAnnotation(FixedRequestParams.class));

        putRequestParams(map, method.getAnnotation(FixedRequestParam.class),
                method.getAnnotation(FixedRequestParams.class));

        return Collections.unmodifiableMap(map);
    }

    private static void putRequestParams(HashMap<String, Object> map,
                                         FixedRequestParam fixedRequestParam,
                                         FixedRequestParams fixedRequestParams) {
        if (fixedRequestParam != null) {
            map.put(fixedRequestParam.name(), fixedRequestParam.value());
        }

        if (fixedRequestParams != null) {
            for (FixedRequestParam paramValue : fixedRequestParams.value()) {
                map.put(paramValue.name(), paramValue.value());
            }
        }
    }


    private static void setStatusMappings(Class<?> restClientImplClass, Object object, Class restClientClass) {
        for (Method method : restClientClass.getDeclaredMethods()) {
            Map<Integer, Class<? extends Throwable>> mappings = createStatusExceptionMappings(method, restClientClass);
            String fieldName = method.getName() + MethodGenerator.StatusExceptionMappings;
            Obj.setField(restClientImplClass, object, fieldName, mappings);
        }
    }

    private static Map<Integer, Class<? extends Throwable>> createStatusExceptionMappings(
            Method method, Class<?> restClientClass) {
        Map<Integer, Class<? extends Throwable>> statusExceptionMappings = Maps.newHashMap();

        addStatusExceptionMapppings(method, statusExceptionMappings, restClientClass.getAnnotation(RespStatusMappings.class));
        addStatusExceptionMapppings(method, statusExceptionMappings, method.getAnnotation(RespStatusMappings.class));

        return Collections.unmodifiableMap(statusExceptionMappings);
    }

    private static void addStatusExceptionMapppings(
            Method method, Map<Integer, Class<? extends Throwable>> statusExceptionMappings,
            RespStatusMappings respStatusMappings) {
        if (respStatusMappings == null) return;

        for (RespStatusMapping respStatusMapping : respStatusMappings.value()) {
            Class<? extends Throwable> exceptionClass = respStatusMapping.exception();
            checkMethodException(method, exceptionClass);
            statusExceptionMappings.put(respStatusMapping.status(), exceptionClass);
        }
    }

    private static void checkMethodException(Method method,
                                             Class<? extends Throwable> exceptionClass) {
        if (RuntimeException.class.isAssignableFrom(exceptionClass)) return;

        // checked exception should be declared
        for (Class<?> declaredExceptionType : method.getExceptionTypes()) {
            if (declaredExceptionType == exceptionClass) return;
        }

        throw new RuntimeException(exceptionClass
                + " is checked exception and should be declared on the method " + method);
    }

    private static void setBaseUrlProvider(Class<?> restClientImplClass,
                                           Object object, Class restClientClass) {
        BaseUrlProvider provider = createBaseUrlProvider(restClientClass);

        Obj.setField(restClientImplClass, object, "baseUrlProvider", provider);
    }

    private static BaseUrlProvider createBaseUrlProvider(Class<?> restClientClass) {
        SpringRestClientEnabled restClientEnabled =
                restClientClass.getAnnotation(SpringRestClientEnabled.class);
        String baseUrl = restClientEnabled.baseUrl();
        if (!Strings.isNullOrEmpty(baseUrl)) return new FixedBaseUrlProvider(baseUrl);

        Class<? extends BaseUrlProvider> providerClass = restClientEnabled.baseUrlProvider();
        if (providerClass == NoneBaseUrlProvider.class) {
            throw new RuntimeException("base url should be configured for api " + restClientClass);
        }

        try {
            return providerClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("base url configuration error for api " + restClientClass, e);
        }
    }


}
