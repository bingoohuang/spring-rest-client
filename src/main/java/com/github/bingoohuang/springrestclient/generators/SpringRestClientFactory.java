package com.github.bingoohuang.springrestclient.generators;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class SpringRestClientFactory {
    private static LoadingCache<Class, Object> restClientCache =
            CacheBuilder.newBuilder().build(new CacheLoader<Class, Object>() {
                @Override
                public Object load(Class restClientClass) throws Exception {
                    ClassGenerator generator = new ClassGenerator(restClientClass);
                    Class<?> restClientImplClass = generator.generate();
                    return createObject(restClientImplClass);
                }
            });

    public static <T> T getRestClient(final Class<T> restClientClass) {
        ensureRestClientClassIsAnInterface(restClientClass);
        return (T) restClientCache.getUnchecked(restClientClass);
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
