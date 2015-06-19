package com.github.bingoohuang.springrestclient.spring.api;

import org.springframework.stereotype.Component;

@Component
public class EasyHiTid {
    public static ThreadLocal<String> tid = new ThreadLocal<String>();

    @Override
    public String toString() {
        return tid.get();
    }
}
