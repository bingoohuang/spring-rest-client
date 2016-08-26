package com.github.bingoohuang.springrestclient.spring.aliyun.utils;

import org.n3r.diamond.client.Miner;
import org.n3r.diamond.client.Minerable;
import org.springframework.stereotype.Component;

@Component
public class PubParamsAccessKeyId {
    @Override public String toString() {
        Minerable miner = new Miner().getMiner("aliyun", "rds");
        return miner.getString("access_keyId");
    }
}
