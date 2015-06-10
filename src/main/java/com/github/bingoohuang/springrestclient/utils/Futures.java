package com.github.bingoohuang.springrestclient.utils;

import com.mashape.unirest.http.HttpResponse;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.Future;

public class Futures {
    public static boolean isFutureReturnType(Method method) {
        Class<?> returnTypeClass = method.getReturnType();
        return Future.class == returnTypeClass;
    }

    public static java.lang.reflect.Type getFutureGenericArgClass(Method method) {
        Class<?> returnTypeClass = method.getReturnType();
        if (Future.class != returnTypeClass) return returnTypeClass;

        java.lang.reflect.Type genericReturnType = method.getGenericReturnType();

        ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
        return parameterizedType.getActualTypeArguments()[0];
    }

    public static Future<Void> convertFutureVoid(final Future<HttpResponse<String>> future,
                                                 final Class<?> beanClass,
                                                 final RestReq restReq) {
        return new VoidFuture(future, restReq);
    }

    public static <T> Future<T> convertFuture(final Future<HttpResponse<String>> future,
                                              final Class<T> beanClass,
                                              final RestReq restReq) {
        return new RestFuture<T>(future, beanClass, restReq);
    }


}
