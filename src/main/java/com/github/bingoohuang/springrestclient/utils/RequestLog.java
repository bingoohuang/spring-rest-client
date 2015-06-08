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

public class RequestLog {
    public static void log(Class<?> apiClass, HttpRequest httpRequest) {
        Logger logger = LoggerFactory.getLogger(apiClass);
        if (logger.isDebugEnabled()) {
            String methodName = httpRequest.getHttpMethod().name();
            String url = httpRequest.getUrl();
            String headers = buildHeaders(httpRequest.getHeaders());
            String body = toString(httpRequest);
            logger.debug("API Request: {} {} headers:{} body: {}", methodName, url, headers, singleLine(body));
        }
    }

    private static String toString(HttpRequest httpRequest) {
        try {
            InputStream context = httpRequest.getBody().getEntity().getContent();
            return new String(ByteStreams.toByteArray(context), Charsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

    public static void log(Class<?> apiClass, HttpResponse<String> response, long costTimeMillis) {
        Logger logger = LoggerFactory.getLogger(apiClass);
        if (logger.isDebugEnabled()) {
            int status = response.getStatus();
            String headers = buildHeaders(response.getHeaders());
            String body = response.getBody();
            logger.debug("API Response: cost {} millis, {} headers:{} body: {}", costTimeMillis, status, headers, singleLine(body));
        }
    }

    private static String buildHeaders(Map<String, List<String>> headers) {
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

    public static String singleLine(String str) {
        String s = lineBreakPattern.matcher(str).replaceAll("\\n");
        return Url.decode(s);
    }
}
