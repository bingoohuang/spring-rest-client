package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.westcache.base.WestCacheKeyer;
import com.github.bingoohuang.westcache.utils.WestCacheOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TidCacheNameGenerator extends WestCacheKeyer {
    @Autowired
    EasyHiTid easyHiTid;

    @Override public String getCacheKey(WestCacheOption option, String methodName, Object bean, Object... args) {
        return "Cache:TidApi:Mobile:" + easyHiTid.toString();
    }
}
