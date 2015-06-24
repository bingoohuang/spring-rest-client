package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrediscache.RedisCacheNameGenerator;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TidCacheNameGenerator implements RedisCacheNameGenerator {
    @Autowired
    EasyHiTid easyHiTid;

    @Override
    public String generateCacheName(MethodInvocation invocation) {
        return "Cache:TidApi:Mobile:" + easyHiTid.toString();
    }
}
