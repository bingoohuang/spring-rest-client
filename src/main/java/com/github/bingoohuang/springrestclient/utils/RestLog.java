package com.github.bingoohuang.springrestclient.utils;

import com.github.bingoohuang.springrestclient.provider.SignProvider;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.ByteStreams;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.request.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

@Slf4j
public class RestLog {
    private final String syncOrAsync;
    private final String uuid = UUID.randomUUID().toString();
    private final Logger logger;
    private final Class<?> apiClass;
    private long start;

    public RestLog(Class<?> apiClass, boolean async) {
        this.apiClass = apiClass;
        this.syncOrAsync = async ? "asyn" : "sync";
        this.logger = LoggerFactory.getLogger(apiClass);
    }

    public void logAndSign(SignProvider signProvider, Map<String, Object> requestParams, HttpRequest httpRequest) {
        if (!logger.isInfoEnabled()) return;

        signReq(signProvider, requestParams, httpRequest);
        this.start = System.currentTimeMillis();
        val methodName = httpRequest.getHttpMethod().name();
        val url = UrlDecodes.decodeQuietly(httpRequest.getUrl());
        val headers = buildHeaders(httpRequest.getHeaders());
        val contentTypes = httpRequest.getHeaders().get("Content-Type");
        val contentType = contentTypes != null && contentTypes.size() > 0 ? contentTypes.get(0) : null;

        val body = getBodyAsString(requestParams, httpRequest);

        logger.info("spring rest client {} {} request: {} {} headers:{} body: {}",
            syncOrAsync, uuid, methodName, url, headers, singleLine(contentType, body));
    }

    private void signReq(SignProvider signProvider, Map<String, Object> requestParams, HttpRequest httpRequest) {
        if (signProvider == null) return;

        signProvider.sign(apiClass, uuid, requestParams, httpRequest);
    }

    private String getBodyAsString(Map<String, Object> requestParams, HttpRequest httpRequest) {
        try {
            val body = httpRequest.getBody();
            if (body == null) return "";

            val entity = body.getEntity();
            // MultipartFormEntity // StringEntity // UrlEncodedFormEntity;
            val context = entity.getContent();
            return new String(ByteStreams.toByteArray(context), Charsets.UTF_8);
        } catch (UnsupportedOperationException e) {
            return requestParams.toString();
        } catch (Exception e) {
            log.warn("exception:{}", e.toString());
            return requestParams.toString();
        }
    }

    public void log(Throwable e) {
        if (!logger.isWarnEnabled()) return;

        val costTimeMillis = System.currentTimeMillis() - start;
        logger.warn("spring rest client {} {} exception: cost {} millis", syncOrAsync, uuid, costTimeMillis, e);
    }

    public void log(HttpResponse<?> response) {
        if (!logger.isInfoEnabled()) return;

        val status = response.getStatus();
        val headers = buildHeaders(response.getHeaders());
        val contentType = response.getHeaders().getFirst("Content-Type");
        val body = response.getBody();
        val costTimeMillis = System.currentTimeMillis() - start;
        if (status >= 200 & status < 300)
            logger.info("spring rest client {} {} response: cost {} millis, {} headers:{} body: {}",
                syncOrAsync, uuid, costTimeMillis, status, headers, singleLine(contentType, body));
        else
            logger.error("spring rest client {} {} response: cost {} millis, {} headers:{} body: {}",
                syncOrAsync, uuid, costTimeMillis, status, headers, singleLine(contentType, body));
    }

    public void log(String status) {
        if (!logger.isInfoEnabled()) return;

        val costTimeMillis = System.currentTimeMillis() - start;
        logger.info("spring rest client {} {} {}: cost {} millis", syncOrAsync, uuid, status, costTimeMillis);
    }

    private String buildHeaders(Map<String, List<String>> headers) {
        return Joiner.on('&').withKeyValueSeparator("=").join(headers);
    }

    static Pattern lineBreakPattern = Pattern.compile("(\\r?\\n)+");

    public String singleLine(String contentType, Object object) {
        if (containsIgnoreCase(contentType, "image")) return "<image>";
        if (object instanceof InputStream) return "<inputstream>";

        val str = "" + object;
        val s = lineBreakPattern.matcher(str).replaceAll("\\n");
        if (containsIgnoreCase(contentType, "json")) return s;

        return UrlDecodes.decodeQuietly(s);
    }

}
