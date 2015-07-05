package com.github.bingoohuang.springrestclient.utils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Future;

public class Types {
    public static java.lang.reflect.Type getFutureGenericArgClass(Method method) {
        Class<?> returnTypeClass = method.getReturnType();
        if (Future.class != returnTypeClass) return returnTypeClass;

        return getGenericTypeArgument(method);
    }

    public static boolean isFutureReturnType(Method method) {
        Class<?> returnTypeClass = method.getReturnType();
        return Future.class == returnTypeClass;
    }

    public static Type getGenericTypeArgument(Method method) {
        Type genericReturnType = method.getGenericReturnType();
        return getGenericTypeArgument(genericReturnType);
    }

    public static Type getGenericTypeArgument(Type genericReturnType) {
        if (!(genericReturnType instanceof ParameterizedType)) return null;

        ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
        return parameterizedType.getActualTypeArguments()[0];
    }
}
