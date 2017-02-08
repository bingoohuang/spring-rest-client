package com.github.bingoohuang.springrestclient.utils;

import com.github.bingoohuang.blackcat.instrument.callback.Blackcat;
import com.mashape.unirest.request.HttpRequest;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/7.
 */
public class Blackcats {
    static final boolean existsBlackCat = classExists(
            "com.github.bingoohuang.blackcat.instrument.callback.Blackcat");

    public static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Throwable e) { // including ClassNotFoundException
            return false;
        }
    }

    public static void prepareRPC(HttpRequest httpRequest) {
        if (!existsBlackCat) return;

        Blackcat.prepareRPC(httpRequest);
    }
}
