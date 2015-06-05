package com.github.bingoohuang.springrestclient.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PrimitiveWrappers {
    private static final Map<Class<?>, String> WRAPPER_XX_VALUE;

    static {
        Map<Class<?>, String> wrapXxValue = new HashMap<Class<?>, String>(8);
        wrapXxValue.put(boolean.class, "booleanValue");
        wrapXxValue.put(char.class, "charValue");
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

    private static final Map<Class<?>, String> WRAPPER_PARSE_XX;

    static {
        Map<Class<?>, String> parseXX = new HashMap<Class<?>, String>(8);
        parseXX.put(boolean.class, "parseBoolean");
//        parseXX.put(char.class, "charValue");
        parseXX.put(byte.class, "parseByte");
        parseXX.put(short.class, "parseShort");
        parseXX.put(int.class, "parseInt");
        parseXX.put(float.class, "parseFloat");
        parseXX.put(long.class, "parseLong");
        parseXX.put(double.class, "parseDouble");

        WRAPPER_PARSE_XX = Collections.unmodifiableMap(parseXX);
    }


    public static String getParseXxMethodName(Class<?> primitiveType) {
        if (!primitiveType.isPrimitive())
            throw new IllegalArgumentException(primitiveType + " is not primitive");

        return WRAPPER_PARSE_XX.get(primitiveType);
    }
}
