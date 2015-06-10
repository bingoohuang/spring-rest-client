package com.github.bingoohuang.springrestclient.utils;

import com.mashape.unirest.http.HttpResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class VoidFuture implements Future<Void>, ResponseAware {
    private final Future<HttpResponse<String>> future;
    private final RestReq restReq;
    private HttpResponse<String> response;

    public VoidFuture(Future<HttpResponse<String>> future,
                      final RestReq restReq) {
        this.future = future;
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
    public Void get() throws InterruptedException, ExecutionException {
        return processReturnVoid(future.get());
    }

    @Override
    public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return processReturnVoid(future.get(timeout, unit));
    }


    private Void processReturnVoid(HttpResponse<String> response) throws ExecutionException {
        this.response = response;
        if (restReq.isSuccessful(response)) return null;

        try {
            throw restReq.processStatusExceptionMappings(response);
        } catch (Throwable throwable) {
            throw new ExecutionException(throwable);
        }
    }

    @Override
    public HttpResponse<String> getResponse() {
        if (response != null) return response;

        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
