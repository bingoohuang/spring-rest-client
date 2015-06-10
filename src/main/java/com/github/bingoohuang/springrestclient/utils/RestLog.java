package com.github.bingoohuang.springrestclient.utils;

import com.github.bingoohuang.utils.net.Url;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.ByteStreams;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RestLog {
    private Class<?> apiClass;
    private String syncOrAsync;
    private long start;

    public RestLog(Class<?> apiClass, String syncOrAsync) {
        this.apiClass = apiClass;
        this.syncOrAsync = syncOrAsync;
    }

    public void log(HttpRequest httpRequest) {
        Logger logger = LoggerFactory.getLogger(apiClass);
        if (logger.isDebugEnabled()) {
            this.start = System.currentTimeMillis();
            String methodName = httpRequest.getHttpMethod().name();
            String url = httpRequest.getUrl();
            String headers = buildHeaders(httpRequest.getHeaders());
            String body = toString(httpRequest);
            logger.debug("API {} request: {} {} headers:{} body: {}",
                    syncOrAsync, methodName, url, headers, singleLine(body));
        }
    }

    private String toString(HttpRequest httpRequest) {
        try {
            InputStream context = httpRequest.getBody().getEntity().getContent();
            return new String(ByteStreams.toByteArray(context), Charsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

    public void log(Throwable e) {
        Logger logger = LoggerFactory.getLogger(apiClass);
        if (logger.isDebugEnabled()) {
            long costTimeMillis = System.currentTimeMillis() - start;
            logger.debug("API {} exception: cost {} millis", syncOrAsync, costTimeMillis, e);
        }
    }

    public void log(HttpResponse<String> response) {
        Logger logger = LoggerFactory.getLogger(apiClass);
        if (logger.isDebugEnabled()) {
            int status = response.getStatus();
            String headers = buildHeaders(response.getHeaders());
            String body = response.getBody();
            long costTimeMillis = System.currentTimeMillis() - start;
            logger.debug("API {} response: cost {} millis, {} headers:{} body: {}",
                    syncOrAsync, costTimeMillis, status, headers, singleLine(body));
        }
    }

    public void log(String status) {
        Logger logger = LoggerFactory.getLogger(apiClass);
        if (logger.isDebugEnabled()) {
            long costTimeMillis = System.currentTimeMillis() - start;
            logger.debug("API {} {}: cost {} millis", syncOrAsync, status, costTimeMillis);
        }
    }

    private String buildHeaders(Map<String, List<String>> headers) {
        StringBuilder sb = new StringBuilder();
        Joiner joiner = Joiner.on(',');

        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            sb.append(entry.getKey())
                    .append('=')
                    .append(joiner.join(entry.getValue()))
                    .append('&');
        }

        if (sb.length() > 0) sb.delete(sb.length() - 1, sb.length());

        return sb.toString();
    }

    static Pattern lineBreakPattern = Pattern.compile("(\\r?\\n)+");

    public String singleLine(String str) {
        String s = lineBreakPattern.matcher(str).replaceAll("\\n");
        return Url.decode(s);
    }

}
