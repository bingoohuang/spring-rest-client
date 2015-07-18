package com.github.bingoohuang.springrestclient.provider;

import com.github.bingoohuang.utils.codec.Base64;
import com.github.bingoohuang.utils.time.Now;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.ValueUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DefaultSignProvider implements SignProvider {
    public static final String CLIENT_KEY = "8d37d3eb-310f-4354-81bb-222e9441e37f";
    public static final String CLIENT_SECURITY = "d51fd93e-f6c9-4eae-ae7a-9b37af1a60cc";

    private final String clientKey;
    private final String clientSecurity;

    public DefaultSignProvider() {
        this.clientKey = CLIENT_KEY;
        this.clientSecurity = CLIENT_SECURITY;
    }

    public DefaultSignProvider(Class<?> apiClass) {
        ClientSign clientSign = apiClass.getAnnotation(ClientSign.class);
        if (clientSign != null) {
            this.clientKey = clientSign.clientKey();
            this.clientSecurity = clientSign.security();
        } else {
            this.clientKey = CLIENT_KEY;
            this.clientSecurity = CLIENT_SECURITY;
        }
    }

    @Override
    public void sign(Class<?> apiClass, String uuid, Map<String, Object> requestParams, HttpRequest httpRequest) {
        httpRequest.header("hict", Now.now());
        httpRequest.header("hici", uuid);
        httpRequest.header("hick", clientKey);
        httpRequest.header("hisa", "hmac");
        httpRequest.header("hisv", hmac(apiClass, requestParams, httpRequest));
    }

    private String hmac(Class<?> apiClass, Map<String, Object> requestParams, HttpRequest httpRequest) {
        String originalStr = createOriginalStringForSign(apiClass, requestParams, httpRequest);
        return hmacSHA256(originalStr, clientSecurity);
    }

    private String createOriginalStringForSign(Class<?> apiClass, Map<String, Object> requestParams, HttpRequest httpRequest) {
        final StringBuilder signStr = new StringBuilder();
        final StringBuilder logStr = new StringBuilder();
        Appendable proxy = new AbbreviateAppendable(logStr, signStr);

        appendMethodAndUrl(httpRequest, proxy);
        appendHeaders(httpRequest, proxy);
        appendRequestParams(requestParams, proxy);

        Logger logger = LoggerFactory.getLogger(apiClass);
        logger.debug("string to be signed : {}", logStr);

        return signStr.toString();
    }

    private void appendMethodAndUrl(HttpRequest httpRequest, Appendable signStr) {
        signStr.append(httpRequest.getHttpMethod().name()).append('$');
        signStr.append(httpRequest.getUrl()).append('$');
    }

    private void appendRequestParams(Map<String, Object> requestParams, Appendable signStr) {
        Map<String, Object> params = Maps.newTreeMap();
        if (requestParams != null) params.putAll(requestParams);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            signStr.append(entry.getKey()).append('$');

            Object value = entry.getValue();
            boolean isFile = false;
            if (value instanceof Collection) {
                isFile = true;
                for (Object s : (Collection) value) {
                    if (s instanceof File || s instanceof MultipartFile)
                        append(signStr, s);
                    else {
                        isFile = false;
                        break;
                    }
                }
            }

            if (!isFile) append(signStr, value);
        }
    }

    private static String[] filtered = new String[]{
            "Content-Type"
    };

    private void appendHeaders(HttpRequest httpRequest, Appendable signStr) {
        Map<String, List<String>> headers = Maps.newTreeMap();
        headers.putAll(httpRequest.getHeaders());

        Joiner joiner = Joiner.on('$');
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String key = entry.getKey();
            if (ArrayUtils.contains(filtered, key)) continue;

            signStr.append(key).append('$')
                    .append(joiner.join(entry.getValue())).append('$');
        }
    }

    private void append(Appendable signStr, Object object) {
        if (object instanceof File) {
            signStr.append(md5((File) object));
        } else if (object instanceof MultipartFile) {
            byte[] bytes = fuckFileGetBytesException((MultipartFile) object);
            signStr.append(md5(bytes));
        } else {
            signStr.appendAbbreviate(ValueUtils.processValue(object));
        }
        signStr.append('$');
    }

    private byte[] fuckFileGetBytesException(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            // fuck this will never happen
            return new byte[0];
        }
    }

    private String md5(File file) {
        try {
            byte[] bytes = Files.toByteArray(file);
            return md5(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(bytes);
            return Base64.base64(digest, Base64.Format.Standard);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hmacSHA256(String data, String key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            byte[] hmacData = mac.doFinal(data.getBytes("UTF-8"));
            return Base64.base64(hmacData, Base64.Format.Standard);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

}
