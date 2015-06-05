package com.github.bingoohuang.springrestclient.generators;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;
import com.github.bingoohuang.springrestclient.provider.FixedBaseUrlProvider;
import com.github.bingoohuang.springrestclient.provider.NoneBaseUrlProvider;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.lang.reflect.Field;

public class SpringRestClientFactory {
    private static LoadingCache<Class, Object> restClientCache =
            CacheBuilder.newBuilder().build(new CacheLoader<Class, Object>() {
                @Override
                public Object load(Class restClientClass) throws Exception {
                    ClassGenerator generator = new ClassGenerator(restClientClass);
                    Class<?> restClientImplClass = generator.generate();
                    Object object = createObject(restClientImplClass);


                    setBaseUrlProvider(restClientImplClass, object, restClientClass);

                    return object;
                }
            });

    private static void setBaseUrlProvider(Class<?> restClientImplClass,
                                           Object object, Class restClientClass) {
        BaseUrlProvider provider = createBaseUrlProvider(restClientClass);
        try {
            Field field = restClientImplClass.getDeclaredField("baseUrlProvider");
            field.setAccessible(true);
            field.set(object, provider);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("unable to set baseUrlProvider field", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("unable to set baseUrlProvider field", e);
        }
    }

    public static <T> T getRestClient(final Class<T> restClientClass) {
        ensureRestClientClassIsAnInterface(restClientClass);
        return (T) restClientCache.getUnchecked(restClientClass);
    }

    public static BaseUrlProvider createBaseUrlProvider(Class<?> restClientClass) {
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

    private static <T> T createObject(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> void ensureRestClientClassIsAnInterface(Class<T> restClientClass) {
        if (restClientClass.isInterface()) return;

        throw new RuntimeException(restClientClass + " is not an interface");
    }
}
