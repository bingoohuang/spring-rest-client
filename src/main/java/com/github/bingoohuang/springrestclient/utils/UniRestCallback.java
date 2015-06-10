package com.github.bingoohuang.springrestclient.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

class UniRestCallback implements Callback<String> {
    private final Class<?> apiClass;
    private final long start;
    private volatile boolean done;
    private volatile boolean cancelled;
    private HttpResponse<String> response;

    public UniRestCallback(final Class<?> apiClass, long start) {
        this.apiClass = apiClass;
        this.start = start;
    }

    @Override
    public void completed(HttpResponse<String> response) {
        this.response = response;
        done = true;
        RestLog.log(apiClass, response, System.currentTimeMillis() - start);
    }

    @Override
    public void failed(UnirestException e) {
        done = true;
        RestLog.log(apiClass, e, System.currentTimeMillis() - start);
    }

    @Override
    public void cancelled() {
        cancelled = true;
        RestLog.log(apiClass, "callback", System.currentTimeMillis() - start);
    }

    public boolean isDone() {
        return done || cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public HttpResponse<String> get() throws InterruptedException {
        while (!isDone()) Thread.sleep(1);
        return response;
    }

    public HttpResponse<String> get(long timeout) throws InterruptedException {
        long start = System.currentTimeMillis();
        while (!isDone() && System.currentTimeMillis() - start < timeout) Thread.sleep(1);

        return response;
    }
}
