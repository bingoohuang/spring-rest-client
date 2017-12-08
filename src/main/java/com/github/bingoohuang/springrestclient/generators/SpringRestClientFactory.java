package com.github.bingoohuang.springrestclient.generators;

import com.github.bingoohuang.springrestclient.annotations.*;
import com.github.bingoohuang.springrestclient.provider.*;
import com.github.bingoohuang.springrestclient.utils.Obj;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.UncheckedExecutionException;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.experimental.var;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@UtilityClass public class SpringRestClientFactory {
    private Cache<Class, Object> restClientCache = CacheBuilder.newBuilder().build();

    public <T> T getRestClient(final Class<T> restClientClass, final ApplicationContext appContext) {
        Obj.ensureInterface(restClientClass);
        try {
            return (T) restClientCache.get(restClientClass, new Callable<Object>() {
                @Override public Object call() {
                    return load(restClientClass, appContext);
                }
            });
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            Throwables.throwIfUnchecked(cause);
            throw new RuntimeException(cause);
        } catch (UncheckedExecutionException e) {
            Throwable cause = e.getCause();
            Throwables.throwIfUnchecked(cause);
            throw new RuntimeException(cause);
        }
    }

    @SneakyThrows
    public Object load(Class restClientClass, ApplicationContext appContext) {
        val generator = new ClassGenerator(restClientClass);
        val restClientImplClass = generator.generate();
        val object = Obj.createObject(restClientImplClass);

        setSignProvider(restClientImplClass, object, restClientClass, appContext);
        setBaseUrlProvider(restClientImplClass, object, restClientClass, appContext);
        setBasicAuthProvider(restClientImplClass, object, restClientClass, appContext);
        setStatusMappings(restClientImplClass, object, restClientClass);
        setFixedRequestParams(restClientImplClass, object, restClientClass);
        setSuccInResponseJSONProperty(restClientImplClass, object, restClientClass);
        setAppContext(restClientImplClass, object, appContext);

        return object;
    }

    private void setAppContext(Class<?> restClientImplClass, Object object, ApplicationContext appContext) {
        Obj.setField(restClientImplClass, object, MethodGenerator.appContext, appContext);
    }

    private void setSuccInResponseJSONProperty(Class<?> restClientImplClass, Object object, Class<?> restClientClass) {
        for (val method : restClientClass.getDeclaredMethods()) {
            var property = method.getAnnotation(SuccInResponseJSONProperty.class);
            if (property == null)
                property = restClientClass.getAnnotation(SuccInResponseJSONProperty.class);

            val fieldName = Obj.getMethodNamePrefixWithHashCode(method) + MethodGenerator.SuccInResponseJSONProperty;
            Obj.setField(restClientImplClass, object, fieldName, property);
        }
    }

    private void setFixedRequestParams(Class<?> restClientImplClass, Object object, Class restClientClass) {
        for (val method : restClientClass.getDeclaredMethods()) {
            val mappings = createFixedRequestParams(method, restClientClass);
            val fieldName = Obj.getMethodNamePrefixWithHashCode(method) + MethodGenerator.FixedRequestParams;
            Obj.setField(restClientImplClass, object, fieldName, mappings);
        }
    }

    private Map<String, Object> createFixedRequestParams(Method method, Class<?> restClientClass) {
        Map<String, Object> map = Maps.newHashMap();

        putRequestParams(map, restClientClass);
        putRequestParams(map, method);

        return Collections.unmodifiableMap(map);
    }

    private void putRequestParams(Map<String, Object> map, AnnotatedElement annotatedElement) {
        // 按声明顺序来添加固定请求参数
        for (val annotation : annotatedElement.getAnnotations()) {
            if (annotation instanceof FixedRequestParam) {
                putFixedRequestParam(map, (FixedRequestParam) annotation);
            } else if (annotation instanceof FixedRequestParams) {
                val params = (FixedRequestParams) annotation;
                for (val paramValue : params.value()) {
                    putFixedRequestParam(map, paramValue);
                }
            }
        }
    }

    private void putFixedRequestParam(Map<String, Object> map, FixedRequestParam fixedRequestParam) {
        if (fixedRequestParam.clazz() != void.class) {
            map.put(fixedRequestParam.name(), fixedRequestParam.clazz());
        } else if (StringUtils.isNotEmpty(fixedRequestParam.value())) {
            map.put(fixedRequestParam.name(), fixedRequestParam.value());
        } else {
            throw new RuntimeException("bad config for @FixedRequestParam"
                    + fixedRequestParam + " value or clazz should be assigned");
        }
    }

    private void setStatusMappings(Class<?> restClientImplClass, Object object, Class restClientClass) {
        for (val method : restClientClass.getDeclaredMethods()) {
            val mappings = createStatusExceptionMappings(method, restClientClass);
            val fieldName = Obj.getMethodNamePrefixWithHashCode(method) + MethodGenerator.StatusExceptionMappings;
            Obj.setField(restClientImplClass, object, fieldName, mappings);
        }
    }

    private Map<Integer, Class<? extends Throwable>> createStatusExceptionMappings(Method method, Class<?> restClientClass) {
        Map<Integer, Class<? extends Throwable>> statusExceptionMappings = Maps.newHashMap();

        addStatusExceptionMapppings(method, statusExceptionMappings, restClientClass.getAnnotation(RespStatusMappings.class));
        addStatusExceptionMapppings(method, statusExceptionMappings, method.getAnnotation(RespStatusMappings.class));

        return Collections.unmodifiableMap(statusExceptionMappings);
    }

    private void addStatusExceptionMapppings(Method method, Map<Integer,
            Class<? extends Throwable>> statusExceptionMappings, RespStatusMappings respStatusMappings) {
        if (respStatusMappings == null) return;

        for (val respStatusMapping : respStatusMappings.value()) {
            val exceptionClass = respStatusMapping.exception();
            checkMethodException(method, exceptionClass);
            statusExceptionMappings.put(respStatusMapping.status(), exceptionClass);
        }
    }

    private void checkMethodException(Method method, Class<? extends Throwable> exceptionClass) {
        if (RuntimeException.class.isAssignableFrom(exceptionClass)) return;

        // checked exception should be declared
        for (val declaredExceptionType : method.getExceptionTypes()) {
            if (declaredExceptionType == exceptionClass) return;
        }

        throw new RuntimeException(exceptionClass + " is checked exception and should be declared on the method " + method);
    }

    private void setBaseUrlProvider(Class<?> restClientImplClass, Object object, Class restClientClass, ApplicationContext appContext) {
        val provider = createBaseUrlProvider(restClientClass, appContext);
        Obj.setField(restClientImplClass, object, MethodGenerator.baseUrlProvider, provider);
    }

    private void setBasicAuthProvider(Class<?> restClientImplClass, Object object, Class restClientClass, ApplicationContext appContext) {
        val provider = createBasicAuthProvider(restClientClass, appContext);
        Obj.setField(restClientImplClass, object, MethodGenerator.basicAuthProvider, provider);
    }

    private void setSignProvider(Class<?> restClientImplClass, Object object, Class restClientClass, ApplicationContext appContext) {
        val provider = createSignProvider(restClientClass, appContext);
        Obj.setField(restClientImplClass, object, MethodGenerator.signProvider, provider);
    }

    private SignProvider createSignProvider(Class<?> restClientClass, ApplicationContext appContext) {
        val restClientEnabled = restClientClass.getAnnotation(SpringRestClientEnabled.class);
        val signProviderClass = restClientEnabled.signProvider();
        val bean = Obj.getBean(appContext, signProviderClass);
        if (bean != null) return bean;

        if (signProviderClass.isInterface()) return null;

        try {
            return signProviderClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("signProvider configuration error for api " + restClientClass, e);
        }
    }

    private BaseUrlProvider createBaseUrlProvider(Class<?> restClientClass, ApplicationContext appContext) {
        val restClientEnabled = restClientClass.getAnnotation(SpringRestClientEnabled.class);
        val baseUrl = restClientEnabled.baseUrl();
        if (!Strings.isNullOrEmpty(baseUrl))
            return new FixedBaseUrlProvider(baseUrl);

        val providerClass = restClientEnabled.baseUrlProvider();
        val bean = Obj.getBean(appContext, providerClass);
        if (bean != null) return bean;

        if (providerClass.isInterface()) {
            throw new RuntimeException("base url should be configured for api " + restClientClass);
        }

        return Obj.createObject(providerClass, restClientClass);
    }

    private BasicAuthProvider createBasicAuthProvider(Class<?> restClientClass, ApplicationContext appContext) {
        val basicAuth = restClientClass.getAnnotation(BasicAuth.class);
        if (basicAuth == null) return null;

        val providerClass = basicAuth.basicAuthProvider();
        val bean = createAuthProvider(appContext, basicAuth, providerClass);

        if (bean != null) return bean;
        if (providerClass.isInterface()) {
            throw new RuntimeException("basicAuthProvider should be properly configured for api " + restClientClass);
        }

        return Obj.createObject(providerClass, restClientClass);
    }

    private static BasicAuthProvider createAuthProvider(ApplicationContext appContext,
                                                        BasicAuth basicAuth,
                                                        Class<? extends BasicAuthProvider> providerClass) {
        if (providerClass == BasicAuthProvider.class) {
            return new DefaultBasicAuthProvider(basicAuth.username(), basicAuth.password());
        } else {
            return Obj.getBean(appContext, providerClass);
        }
    }
}
