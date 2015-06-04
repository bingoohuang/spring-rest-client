package com.github.bingoohuang.springrestclient.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AsmUtils {
    /**
     * A map from wrapper types to their corresponding primitive types.
     */
    private static final Map<Class<?>, String> WRAPPER_XX_VALUE;

    static {
        Map<Class<?>, String> wrapXxValue = new HashMap<Class<?>, String>(8);
        wrapXxValue.put(boolean.class, "booleanValue");
        wrapXxValue.put(Character.class, "charValue");
        wrapXxValue.put(byte.class, "byteValue");
        wrapXxValue.put(short.class, "shortValue");
        wrapXxValue.put(int.class, "intValue");
        wrapXxValue.put(float.class, "floatValue");
        wrapXxValue.put(long.class, "longValue");
        wrapXxValue.put(double.class, "doubleValue");

        WRAPPER_XX_VALUE = Collections.unmodifiableMap(wrapXxValue);
    }


    public static String getXxValueMethodName(Class<?> primitiveType) {
        if (!primitiveType.isPrimitive())
            throw new IllegalArgumentException(primitiveType + " is not primitive");

        return WRAPPER_XX_VALUE.get(primitiveType);
    }
}
