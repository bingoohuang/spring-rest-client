package com.github.bingoohuang.springrestclient.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class Obj {

    public static <T> T createObject(Class<T> clazz, Object ctorArg) {
        try {
            Constructor<T> constructor = clazz.getConstructor(ctorArg.getClass());
            return constructor.newInstance(ctorArg);
        } catch (NoSuchMethodException ex) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return createObject(clazz);
    }


    public static <T> T createObject(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void setField(Class<?> clazz, Object object, String fieldName, Object fieldValue) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, fieldValue);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("unable to set field " + fieldName, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("unable to set field " + fieldName, e);
        }
    }


    public static <T> void ensureInterface(Class<T> clazz) {
        if (clazz.isInterface()) return;

        throw new RuntimeException(clazz + " is not an interface");
    }

}
