package com.github.bingoohuang.springrestclient.utils;

import lombok.experimental.UtilityClass;
import lombok.val;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Future;

@UtilityClass
public class Types {
    public java.lang.reflect.Type getFutureGenericArgClass(Method method) {
        val returnTypeClass = method.getReturnType();
        if (Future.class != returnTypeClass) return returnTypeClass;

        return getGenericTypeArgument(method);
    }

    public boolean isFutureReturnType(Method method) {
        val returnTypeClass = method.getReturnType();
        return Future.class == returnTypeClass;
    }

    public Type getGenericTypeArgument(Method method) {
        val genericReturnType = method.getGenericReturnType();
        return getGenericTypeArgument(genericReturnType);
    }

    public Type getGenericTypeArgument(Type genericReturnType) {
        if (!(genericReturnType instanceof ParameterizedType)) return null;

        val parameterizedType = (ParameterizedType) genericReturnType;
        return parameterizedType.getActualTypeArguments()[0];
    }
}
