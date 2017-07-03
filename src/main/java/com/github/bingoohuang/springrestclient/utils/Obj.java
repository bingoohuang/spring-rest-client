package com.github.bingoohuang.springrestclient.utils;

import com.google.common.primitives.UnsignedInts;
import lombok.experimental.UtilityClass;
import org.objectweb.asm.Type;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@UtilityClass
public class Obj {
    public Object getOrCreateBean(ApplicationContext appContext, Class<?> requiredType) {
        try {
            return appContext.getBean(requiredType);
        } catch (NoSuchBeanDefinitionException e) {
            return createObject(requiredType);
        }
    }

    public <T> T getBean(ApplicationContext appContext, Class<T> beanClass) {
        try {
            return appContext.getBean(beanClass);
        } catch (BeansException e) {
            return null;
        }
    }

    public <T> T createObject(Class<T> clazz, Object ctorArg) {
        try {
            Constructor<T> constructor = clazz.getConstructor(ctorArg.getClass());
            return constructor.newInstance(ctorArg);
        } catch (NoSuchMethodException ex) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return createObject(clazz);
    }


    public <T> T createObject(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void setField(Class<?> clazz, Object object, String fieldName, Object fieldValue) {
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

    public String getMethodNamePrefixWithHashCode(Method method) {
        String methodDescriptor = Type.getMethodDescriptor(method);
        return method.getName() + "$" + UnsignedInts.toString(methodDescriptor.hashCode()) + "$";
    }


    public <T> void ensureInterface(Class<T> clazz) {
        if (clazz.isInterface()) return;

        throw new RuntimeException(clazz + " is not an interface");
    }

}
