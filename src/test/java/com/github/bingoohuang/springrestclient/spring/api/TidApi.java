package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrediscache.RedisCacheEnabled;
import com.github.bingoohuang.springrestclient.annotations.FixedRequestParam;
import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.provider.DefaultSignProvider;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RequestMapping("/tid")
//@RedisCacheTargetMock("com.github.bingoohuang.springrestclient.spring.mock.MockTidApi")
@SpringRestClientEnabled(baseUrl = "http://localhost:4849", signProvider = DefaultSignProvider.class)
public interface TidApi {
    @RequestMapping(value = "/get-mobile", method = GET)
    @FixedRequestParam(name = "tid", clazz = EasyHiTid.class)
    String getMobile();

    @RequestMapping(value = "/get-mobile", method = GET)
    @FixedRequestParam(name = "tid", clazz = EasyHiTid.class)
    @RedisCacheEnabled(expirationSeconds = 61, naming = TidCacheNameGenerator.class)
    String getMobile2();
}
