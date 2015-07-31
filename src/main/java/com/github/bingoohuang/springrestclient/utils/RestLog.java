package com.github.bingoohuang.springrestclient.utils;

import com.github.bingoohuang.springrestclient.provider.SignProvider;
import com.github.bingoohuang.utils.net.Url;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.ByteStreams;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.Body;
import org.apache.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class RestLog {
    private final String syncOrAsync;
    private final String uuid = UUID.randomUUID().toString();
    private final Logger logger;
    private final Class<?> apiClass;
    private long start;
    Logger log = LoggerFactory.getLogger(RestLog.class);

    public RestLog(Class<?> apiClass, boolean async) {
        this.apiClass = apiClass;
        this.syncOrAsync = async ? "asyn" : "sync";
        this.logger = LoggerFactory.getLogger(apiClass);
    }

    public void logAndSign(SignProvider signProvider, Map<String, Object> requestParams, HttpRequest httpRequest) {
        if (!logger.isInfoEnabled()) return;

        signReq(signProvider, requestParams, httpRequest);
        this.start = System.currentTimeMillis();
        String methodName = httpRequest.getHttpMethod().name();
        String url = httpRequest.getUrl();
        String headers = buildHeaders(httpRequest.getHeaders());
        String body = getBodyAsString(requestParams, httpRequest);

        logger.info("spring rest client {} {} request: {} {} headers:{} body: {}",
                syncOrAsync, uuid, methodName, url, headers, singleLine(body));
    }

    private void signReq(SignProvider signProvider, Map<String, Object> requestParams, HttpRequest httpRequest) {
        if (signProvider == null) return;

        signProvider.sign(apiClass, uuid, requestParams, httpRequest);
    }

    private String getBodyAsString(Map<String, Object> requestParams, HttpRequest httpRequest) {
        try {
            Body body = httpRequest.getBody();
            if (body == null) return "";

            HttpEntity entity = body.getEntity();
            // MultipartFormEntity // StringEntity // UrlEncodedFormEntity;
            InputStream context = entity.getContent();
            return new String(ByteStreams.toByteArray(context), Charsets.UTF_8);
        } catch (Exception e) {
            log.warn("exception", e);
            return requestParams.toString();
        }
    }

    public void log(Throwable e) {
        if (!logger.isWarnEnabled()) return;

        long costTimeMillis = System.currentTimeMillis() - start;
        logger.warn("spring rest client {} {} exception: cost {} millis", syncOrAsync, uuid, costTimeMillis, e);
    }

    public void log(HttpResponse<?> response) {
        if (!logger.isInfoEnabled()) return;

        int status = response.getStatus();
        String headers = buildHeaders(response.getHeaders());
        Object body = response.getBody();
        long costTimeMillis = System.currentTimeMillis() - start;
        if (status >= 200 & status < 300)
            logger.info("spring rest client {} {} response: cost {} millis, {} headers:{} body: {}",
                    syncOrAsync, uuid, costTimeMillis, status, headers, singleLine(body));
        else
            logger.error("spring rest client {} {} response: cost {} millis, {} headers:{} body: {}",
                    syncOrAsync, uuid, costTimeMillis, status, headers, singleLine(body));
    }

    public void log(String status) {
        if (!logger.isInfoEnabled()) return;

        long costTimeMillis = System.currentTimeMillis() - start;
        logger.info("spring rest client {} {} {}: cost {} millis", syncOrAsync, uuid, status, costTimeMillis);
    }

    private String buildHeaders(Map<String, List<String>> headers) {
        return Joiner.on('&').withKeyValueSeparator("=").join(headers);
    }

    static Pattern lineBreakPattern = Pattern.compile("(\\r?\\n)+");

    public String singleLine(Object object) {
        if (object instanceof InputStream) return "inputstream";

        String str = "" + object;
        String s = lineBreakPattern.matcher(str).replaceAll("\\n");
        return Url.decode(s);
    }

}
