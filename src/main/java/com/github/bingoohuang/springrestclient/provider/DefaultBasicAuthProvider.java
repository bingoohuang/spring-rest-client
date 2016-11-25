package com.github.bingoohuang.springrestclient.provider;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2016/11/25.
 */
public class DefaultBasicAuthProvider implements BasicAuthProvider {
    final String username;
    final String password;

    public DefaultBasicAuthProvider(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override public String username() {
        return username;
    }

    @Override public String password() {
        return password;
    }
}
