package com.github.bingoohuang.springrestclient.utils;

import com.alibaba.fastjson.JSON;
import com.mashape.unirest.http.HttpResponse;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RestFuture<T> implements Future<T>, ResponseAware {
    private final Future<HttpResponse<?>> future;
    private final Class<T> beanClass;
    private final RestReq restReq;
    private HttpResponse<?> response;

    public RestFuture(Future<HttpResponse<?>> future,
                      final Class<T> beanClass,
                      final RestReq restReq) {
        this.future = future;
        this.beanClass = beanClass;
        this.restReq = restReq;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return future.isCancelled();
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public HttpResponse<?> getResponse() {
        if (response != null) return response;

        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return processReturn(future.get());
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return processReturn(future.get(timeout, unit));
    }

    private T processReturn(HttpResponse<?> response) throws ExecutionException {
        this.response = response;

        if (restReq.isSuccessful(response)) {
            Object body = restReq.nullOrBody(response);
            if (body == null) return null;
            if (beanClass == InputStream.class) return (T) response.getRawBody();

            T bean = JSON.parseObject("" + body, beanClass);
            return bean;
        }

        try {
            throw restReq.processStatusExceptionMappings(response);
        } catch (Throwable throwable) {
            throw new ExecutionException(throwable);
        }
    }
}
