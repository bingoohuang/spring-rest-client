package com.github.bingoohuang.springrestclient.generators;

import com.github.bingoohuang.springrestclient.annotations.*;
import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;
import com.github.bingoohuang.springrestclient.provider.FixedBaseUrlProvider;
import com.github.bingoohuang.springrestclient.provider.SignProvider;
import com.github.bingoohuang.springrestclient.utils.Obj;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class SpringRestClientFactory {
    private static Cache<Class, Object> restClientCache = CacheBuilder.newBuilder().build();


    public static <T> T getRestClient(final Class<T> restClientClass, final ApplicationContext appContext) {
        Obj.ensureInterface(restClientClass);
        try {
            return (T) restClientCache.get(restClientClass, new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return load(restClientClass, appContext);
                }
            });
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            throw Throwables.propagate(cause);
        } catch (UncheckedExecutionException e) {
            Throwable cause = e.getCause();
            throw Throwables.propagate(cause);
        }
    }

    public static Object load(Class restClientClass, ApplicationContext appContext) throws Exception {
        ClassGenerator generator = new ClassGenerator(restClientClass);
        Class<?> restClientImplClass = generator.generate();
        Object object = Obj.createObject(restClientImplClass);

        setSignProvider(restClientImplClass, object, restClientClass, appContext);
        setBaseUrlProvider(restClientImplClass, object, restClientClass, appContext);
        setStatusMappings(restClientImplClass, object, restClientClass);
        setFixedRequestParams(restClientImplClass, object, restClientClass);
        setSuccInResponseJSONProperty(restClientImplClass, object, restClientClass);
        setAppContext(restClientImplClass, object, restClientClass, appContext);

        return object;
    }

    private static void setAppContext(Class<?> restClientImplClass, Object object,
                                      Class restClientClass, ApplicationContext appContext) {
        Obj.setField(restClientImplClass, object, MethodGenerator.appContext, appContext);
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
        Map<String, Object> map = Maps.newHashMap();

        putRequestParams(map, restClientClass);
        putRequestParams(map, method);

        return Collections.unmodifiableMap(map);
    }

    private static void putRequestParams(Map<String, Object> map, AnnotatedElement accessibleObject) {
        // 按声明顺序来添加固定请求参数
        for (Annotation annotation : accessibleObject.getAnnotations()) {
            if (annotation instanceof FixedRequestParam) {
                putFixedRequestParam(map, (FixedRequestParam) annotation);
            } else if (annotation instanceof FixedRequestParams) {
                for (FixedRequestParam paramValue : ((FixedRequestParams) annotation).value()) {
                    putFixedRequestParam(map, paramValue);
                }
            }
        }
    }

    private static void putFixedRequestParam(Map<String, Object> map, FixedRequestParam fixedRequestParam) {
        if (fixedRequestParam.clazz() != void.class) {
            map.put(fixedRequestParam.name(), fixedRequestParam.clazz());
        } else if (StringUtils.isNotEmpty(fixedRequestParam.value())) {
            map.put(fixedRequestParam.name(), fixedRequestParam.value());
        } else {
            throw new RuntimeException("bad config for @FixedRequestParam" + fixedRequestParam
                    + " value or clazz should be assigned");
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

    private static void setBaseUrlProvider(Class<?> restClientImplClass, Object object, Class restClientClass, ApplicationContext appContext) {
        BaseUrlProvider provider = createBaseUrlProvider(restClientClass, appContext);
        Obj.setField(restClientImplClass, object, MethodGenerator.baseUrlProvider, provider);
    }

    private static void setSignProvider(Class<?> restClientImplClass, Object object, Class restClientClass, ApplicationContext appContext) {
        SignProvider provider = createSignProvider(restClientClass, appContext);
        Obj.setField(restClientImplClass, object, MethodGenerator.signProvider, provider);
    }

    private static SignProvider createSignProvider(Class<?> restClientClass, ApplicationContext appContext) {
        SpringRestClientEnabled restClientEnabled = restClientClass.getAnnotation(SpringRestClientEnabled.class);
        Class<? extends SignProvider> signProviderClass = restClientEnabled.signProvider();
        SignProvider bean = Obj.getBean(appContext, signProviderClass);
        if (bean != null) return bean;

        if (signProviderClass.isInterface()) return null;

        try {
            return signProviderClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("signProvider configuration error for api " + restClientClass, e);
        }
    }

    private static BaseUrlProvider createBaseUrlProvider(Class<?> restClientClass, ApplicationContext appContext) {
        SpringRestClientEnabled restClientEnabled = restClientClass.getAnnotation(SpringRestClientEnabled.class);
        String baseUrl = restClientEnabled.baseUrl();
        if (!Strings.isNullOrEmpty(baseUrl)) return new FixedBaseUrlProvider(baseUrl);

        Class<? extends BaseUrlProvider> providerClass = restClientEnabled.baseUrlProvider();
        BaseUrlProvider bean = Obj.getBean(appContext, providerClass);
        if (bean != null) return bean;

        if (providerClass.isInterface()) {
            throw new RuntimeException("base url should be configured for api " + restClientClass);
        }

        return Obj.createObject(providerClass, restClientClass);
    }
}
