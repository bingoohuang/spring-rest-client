package com.github.bingoohuang.springrestclient.boot.filter;

import org.apache.commons.io.output.TeeOutputStream;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

@Component
public class MultiReadFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // use wrapper to read multiple times the content
        MultiReadHttpServletRequest req = new MultiReadHttpServletRequest((HttpServletRequest) request);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        req.setAttribute("_log_baos", baos);

        chain.doFilter(req, new HttpServletResponseWrapper((HttpServletResponse) response) {
            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return new DelegatingServletOutputStream(new TeeOutputStream(super.getOutputStream(), baos)
                );
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                return new PrintWriter(new DelegatingServletOutputStream(new TeeOutputStream(super.getOutputStream(), baos))
                );
            }
        });
    }

    @Override
    public void destroy() {

    }
}
