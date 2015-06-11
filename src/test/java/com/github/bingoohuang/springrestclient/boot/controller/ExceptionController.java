package com.github.bingoohuang.springrestclient.boot.controller;

import com.github.bingoohuang.springrestclient.boot.exception.BadArgumentException;
import com.github.bingoohuang.springrestclient.boot.exception.NotFoundException;
import com.github.bingoohuang.springrestclient.boot.exception.RestException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/exception")
public class ExceptionController {
    @RequestMapping("/error/{error}")
    public int error(@PathVariable("error") int error) {
        if (error == 1) throw new NotFoundException("NotFoundException ErrorMsg");
        if (error == 2) throw new BadArgumentException("BadArgumentException ErrorMsg");
        if (error == 3) throw new RuntimeException("RuntimeException ErrorMsg");
        if (error == 4) throw new RestException(406, "RestException ErrorMsg");
        else return error;
    }

    // https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
    // Controller Based Exception Handling
    @ExceptionHandler(BadArgumentException.class)
    public void badArgumentExceptionHandler(HttpServletResponse response, BadArgumentException ex) {
        response.setStatus(405);
        response.addHeader("error-msg", ex.getMessage());
    }
}
