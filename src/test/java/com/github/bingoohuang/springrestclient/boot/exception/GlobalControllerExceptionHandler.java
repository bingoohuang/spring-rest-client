package com.github.bingoohuang.springrestclient.boot.exception;

import com.github.bingoohuang.springrestclient.boot.controller.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public void handleConflict(NotFoundException ex, HttpServletResponse response) {
        response.addHeader("error-msg", ex.getMessage());
    }

    @ExceptionHandler(RestException.class)
    public void handleConflict(RestException ex, HttpServletResponse response) {
        response.setStatus(ex.getHttpStatusCode());
        response.addHeader("error-msg", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public void handleConflict(Exception ex, HttpServletResponse response) {
        response.setStatus(500);
        response.addHeader("error-msg", ex.getMessage());
    }
}
