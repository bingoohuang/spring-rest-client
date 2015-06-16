package com.github.bingoohuang.springrestclient.boot;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Http {
    public static void respondText(HttpServletResponse rsp, String text) {
        try {
            rsp.setHeader("Content-Type", "text/plain; charset=UTF-8");
            rsp.setCharacterEncoding("UTF-8");
            if (text != null) {
                PrintWriter writer = rsp.getWriter();
                writer.write(text);
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void error(HttpServletResponse response, int statusCode, Throwable ex) {
        response.setStatus(statusCode);
        respondText(response, ex.getMessage());
    }


    public static void error(HttpServletResponse response, int statusCode, String message) {
        response.setStatus(statusCode);
        respondText(response, message);
    }
}
