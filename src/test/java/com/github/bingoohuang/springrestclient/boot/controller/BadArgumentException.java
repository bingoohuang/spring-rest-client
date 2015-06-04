package com.github.bingoohuang.springrestclient.boot.controller;

public class BadArgumentException extends RuntimeException {
    public BadArgumentException(String msg) {
        super(msg);
    }
}
