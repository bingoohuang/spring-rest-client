package com.github.bingoohuang.springrestclient.boot.exception;

// https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
// Using HTTP Status Codes
public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg) {
        super(msg);
    }
}
