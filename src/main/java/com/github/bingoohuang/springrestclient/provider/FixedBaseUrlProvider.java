package com.github.bingoohuang.springrestclient.provider;

public class FixedBaseUrlProvider implements BaseUrlProvider {
    private final String baseUrl;

    public FixedBaseUrlProvider(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String getBaseUrl(Class<?> apiClass) {
        return baseUrl;
    }
}
