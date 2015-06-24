package com.github.bingoohuang.springrestclient.tests.api;

import org.springframework.aop.TargetSource;

import java.lang.reflect.Method;

public class CglibHelper {
    private final Object proxied;

    public CglibHelper(Object proxied) {
        this.proxied = proxied;
    }

    public Object getTargetObject() {
        String name = proxied.getClass().getName();
        if (name.toLowerCase().contains("cglib")) {
            return extractTargetObject(proxied);
        }
        return proxied;
    }

    private Object extractTargetObject(Object proxied) {
        try {
            TargetSource springTargetSource = findSpringTargetSource(proxied);
            return springTargetSource.getTarget();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TargetSource findSpringTargetSource(Object proxied) {
        Method[] methods = proxied.getClass().getDeclaredMethods();
        Method targetSourceMethod = findTargetSourceMethod(methods);
        targetSourceMethod.setAccessible(true);
        try {
            return (TargetSource) targetSourceMethod.invoke(proxied);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Method findTargetSourceMethod(Method[] methods) {
        for (Method method : methods) {
            if (method.getName().endsWith("getTargetSource")) {
                return method;
            }
        }
        throw new IllegalStateException(
                "Could not find target source method on proxied object ["
                        + proxied.getClass() + "]");
    }
}
