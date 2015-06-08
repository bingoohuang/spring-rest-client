package com.github.bingoohuang.springrestclient.boot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
// Using HTTP Status Codes
@ResponseStatus(value = HttpStatus.NOT_FOUND)  // 404
public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg) {
        super(msg);
    }
}
