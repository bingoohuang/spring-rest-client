package com.github.bingoohuang.springrestclient.provider;

import org.springframework.stereotype.Component;

@Component
public class FixedBaseUrlProvider implements BaseUrlProvider {
    private String baseUrl;

    public FixedBaseUrlProvider() {
    }

    public FixedBaseUrlProvider(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String getBaseUrl(Class<?> apiClass) {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
