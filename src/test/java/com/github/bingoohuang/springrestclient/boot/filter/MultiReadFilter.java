package com.github.bingoohuang.springrestclient.boot.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class MultiReadFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // use wrapper to read multiple times the content
        MultiReadHttpServletRequest req = new MultiReadHttpServletRequest((HttpServletRequest) request);
        chain.doFilter(req, response);
    }

    @Override
    public void destroy() {

    }
}
