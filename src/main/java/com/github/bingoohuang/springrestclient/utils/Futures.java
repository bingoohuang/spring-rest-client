package com.github.bingoohuang.springrestclient.utils;

import com.github.bingoohuang.utils.codec.Json;
import com.mashape.unirest.http.HttpResponse;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Future;

public class Futures {
    public static boolean isFutureReturnType(Method method) {
        Class<?> returnTypeClass = method.getReturnType();
        return Future.class == returnTypeClass;
    }

    public static java.lang.reflect.Type getFutureGenericArgClass(Method method) {
        Class<?> returnTypeClass = method.getReturnType();
        if (Future.class != returnTypeClass) return returnTypeClass;

        return getGenericTypeArgument(method);
    }

    public static Type getGenericTypeArgument(Method method) {
        Type genericReturnType = method.getGenericReturnType();
        if (!(genericReturnType instanceof ParameterizedType)) return null;

        ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
        return parameterizedType.getActualTypeArguments()[0];
    }

    public static Future<Void> convertFutureVoid(final Future<HttpResponse<?>> future,
                                                 final Class<?> beanClass,
                                                 final RestReq restReq) {
        return new VoidFuture(future, restReq);
    }

    public static <T> Future<T> convertFuture(final Future<HttpResponse<?>> future,
                                              final Class<T> beanClass,
                                              final RestReq restReq) {
        return new RestFuture<T>(future, beanClass, restReq);
    }


    public static Object convertReturn(String text, Class<?> clazz) {
        return Json.unJson(text, clazz);
    }

    public static Object convertArrReturn(String text, Class<?> clazz) {
        return Json.unJsonArray(text, clazz);
    }
}
