package com.github.bingoohuang.springrestclient.boot.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public void handleConflict(NotFoundException ex, HttpServletResponse response) {
        response.setStatus(404);
        respondText(response, ex.getMessage());
    }

    @ExceptionHandler(RestException.class)
    public void handleConflict(RestException ex, HttpServletResponse response) {
        response.setStatus(ex.getHttpStatusCode());
        respondText(response, ex.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public void handleConflict(Throwable ex, HttpServletResponse response) {
        response.setStatus(500);
        respondText(response, ex.getMessage());
    }

    public static void respondText(HttpServletResponse rsp, String text) {
        try {
            rsp.setHeader("Content-Type", "text/plain; charset=UTF-8");
            rsp.setCharacterEncoding("UTF-8");
            PrintWriter writer = rsp.getWriter();
            writer.write(text);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
