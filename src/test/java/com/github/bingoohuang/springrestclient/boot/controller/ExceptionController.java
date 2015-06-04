package com.github.bingoohuang.springrestclient.boot.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ExceptionController {
    @RequestMapping("/exception1/{error}")
    public int exception1(@PathVariable("error") int error) throws NotFoundException {
        if (error == 1) throw new NotFoundException("bad argument    ");
        if (error == 2) throw new BadArgumentException("bad argument    ");
        else return error;
    }

    // https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
    // Controller Based Exception Handling
    @ExceptionHandler(BadArgumentException.class)
    public void badArgumentExceptionHandler(HttpServletResponse response, BadArgumentException ex) {
        response.setStatus(405);
        response.addHeader("exception-name", ex.getClass().getName());
        response.addHeader("exception-msg", ex.getMessage());
    }
}
