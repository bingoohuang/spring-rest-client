package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.provider.BasicAuthProvider;
import org.springframework.stereotype.Component;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2016/11/25.
 */
@Component
public class DynamicBasicAuthProvider implements BasicAuthProvider {
    @Override public String username() {
        return "username";
    }

    @Override public String password() {
        return "password123";
    }
}
