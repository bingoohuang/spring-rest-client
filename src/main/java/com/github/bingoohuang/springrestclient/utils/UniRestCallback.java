package com.github.bingoohuang.springrestclient.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

class UniRestCallback<T> implements Callback<T> {
    private final Class<?> apiClass;
    private final RestLog restLog;
    private volatile boolean done;
    private volatile boolean cancelled;
    private HttpResponse<T> response;

    public UniRestCallback(final Class<?> apiClass, RestLog restLog) {
        this.apiClass = apiClass;
        this.restLog = restLog;
    }

    @Override
    public void completed(HttpResponse<T> response) {
        this.response = response;
        done = true;
        restLog.log(response);
    }

    @Override
    public void failed(UnirestException e) {
        done = true;
        restLog.log(e);
    }

    @Override
    public void cancelled() {
        cancelled = true;
        restLog.log("cancelled");
    }

    public boolean isDone() {
        return done || cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public HttpResponse<T> get() throws InterruptedException {
        while (!isDone()) Thread.sleep(1);
        return response;
    }

    public HttpResponse<T> get(long timeout) throws InterruptedException {
        long start = System.currentTimeMillis();
        while (!isDone() && System.currentTimeMillis() - start < timeout) Thread.sleep(1);

        return response;
    }
}
