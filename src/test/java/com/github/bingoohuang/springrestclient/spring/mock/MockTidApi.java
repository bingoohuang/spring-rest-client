package com.github.bingoohuang.springrestclient.spring.mock;

import com.github.bingoohuang.springrestclient.spring.api.TidApi;
import org.springframework.stereotype.Component;

@Component
public class MockTidApi implements TidApi {
    private String mockedValue;

    @Override
    public String getMobile() {
        return null;
    }

    @Override
    public String getMobile2() {
        return mockedValue;
    }

    public void setMockedValue(String mockedValue) {
        this.mockedValue = mockedValue;
    }
}
