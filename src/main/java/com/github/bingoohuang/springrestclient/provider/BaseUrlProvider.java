package com.github.bingoohuang.springrestclient.provider;

public interface BaseUrlProvider {
    String getBaseUrl(Class<?> apiClass);
}
