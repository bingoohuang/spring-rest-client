package com.github.bingoohuang.springrestclient.spring.aliyun.utils;

import org.n3r.idworker.Id;
import org.springframework.stereotype.Component;

@Component
public class PubParamsSignatureNonce {
    @Override public String toString() {
        return "" + Id.next();
    }
}
