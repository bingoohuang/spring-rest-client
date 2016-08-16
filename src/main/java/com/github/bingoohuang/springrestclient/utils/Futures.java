package com.github.bingoohuang.springrestclient.utils;

import com.mashape.unirest.http.HttpResponse;
import lombok.experimental.UtilityClass;

import java.util.concurrent.Future;

@UtilityClass
public class Futures {
    public Future<Void> convertFutureVoid(final Future<HttpResponse<?>> future,
                                          final Class<?> beanClass,
                                          final RestReq restReq) {
        return new VoidFuture(future, restReq);
    }

    public <T> Future<T> convertFuture(final Future<HttpResponse<?>> future,
                                       final Class<T> beanClass,
                                       final RestReq restReq) {
        return new RestFuture<T>(future, beanClass, restReq);
    }
}
