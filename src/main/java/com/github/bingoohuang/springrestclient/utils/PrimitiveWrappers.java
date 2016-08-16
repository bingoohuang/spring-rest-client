package com.github.bingoohuang.springrestclient.utils;

import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class PrimitiveWrappers {
    private final Map<Class<?>, String> WRAPPER_XX_VALUE;

    static {
        val wrapXxValue = new HashMap<Class<?>, String>(8);
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


    public String getXxValueMethodName(Class<?> primitiveType) {
        if (!primitiveType.isPrimitive())
            throw new IllegalArgumentException(primitiveType + " is not primitive");

        return WRAPPER_XX_VALUE.get(primitiveType);
    }

    private final Map<Class<?>, String> WRAPPER_PARSE_XX;

    static {
        val parseXX = new HashMap<Class<?>, String>(8);
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


    public String getParseXxMethodName(Class<?> primitiveType) {
        if (!primitiveType.isPrimitive())
            throw new IllegalArgumentException(primitiveType + " is not primitive");

        return WRAPPER_PARSE_XX.get(primitiveType);
    }
}
