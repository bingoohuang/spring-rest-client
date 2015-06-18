package com.github.bingoohuang.springrestclient.utils;

import com.mashape.unirest.http.HttpResponse;

public interface ResponseAware {
    HttpResponse<?> getResponse();
}
