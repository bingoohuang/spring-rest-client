package com.github.bingoohuang.springrestclient.generators;

public class RestClientClassLoader extends ClassLoader {
    public RestClientClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class<?> defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}
