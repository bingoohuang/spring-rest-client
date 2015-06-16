package com.github.bingoohuang.springrestclient.boot.interceptor;

import com.github.bingoohuang.springrestclient.boot.Http;
import com.github.bingoohuang.springrestclient.boot.annotations.RestfulSign;
import com.github.bingoohuang.utils.codec.Base64;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class SignInterceptor extends HandlerInterceptorAdapter {
    public static final String CLIENT_SECURITY = "d51fd93e-f6c9-4eae-ae7a-9b37af1a60cc";

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) return false;

        HandlerMethod method = (HandlerMethod) handler;
        Class<?> beanType = method.getBeanType();
        boolean ignoreSign = ignoreSign(beanType, method);
        Logger logger = LoggerFactory.getLogger("rest." + beanType.getName());

        if (ignoreSign && !logger.isInfoEnabled()) return true;

        String originalStr = createOriginalStringForSign(request);
        logger.info("spring rest server {}", originalStr);

        if (ignoreSign) return true;

        String hisv = request.getHeader("hisv");
        if (Strings.isNullOrEmpty(hisv)) {
            Http.error(response, 416, "signature missed");
            return false;
        }

        String sign = hmacSHA256(originalStr, CLIENT_SECURITY);
        boolean signOk = sign.equals(hisv);
        if (!signOk) Http.error(response, 416, "invalid signature");

        return signOk;
    }

    private String createOriginalStringForSign(HttpServletRequest request) {
        StringBuilder signStr = new StringBuilder();
        appendMethodAndUrl(request, signStr);
        appendHeaders(request, signStr);
        appendRequestParams(request, signStr);

        return signStr.toString();
    }

    private boolean ignoreSign(Class<?> beanType, HandlerMethod method) {
        RestfulSign restfulSign = method.getMethod().getAnnotation(RestfulSign.class);
        if (restfulSign != null && restfulSign.ignore()) return true;

        restfulSign = beanType.getAnnotation(RestfulSign.class);
        return restfulSign != null && restfulSign.ignore();
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

    private void appendRequestParams(HttpServletRequest request, StringBuilder signStr) {
        Map<String, String[]> parameterMap = Maps.newTreeMap();
        parameterMap.putAll(request.getParameterMap());

        String json = getJson(request);
        if (!Strings.isNullOrEmpty(json)) parameterMap.put("_json", new String[]{json});
        fileUpload(request, parameterMap);

        String queryString = request.getQueryString();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String parameterName = entry.getKey();
            if (isQueryParameter(queryString, parameterName)) continue;


            signStr.append(parameterName).append('$');
            for (String value : entry.getValue()) {
                signStr.append(value).append('$');
            }
        }
    }

    private void fileUpload(HttpServletRequest request, Map<String, String[]> parameterMap) {
        if (!(request instanceof MultipartHttpServletRequest)) return;

        MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> multiFileMap = mreq.getMultiFileMap();

        for (Map.Entry<String, List<MultipartFile>> entry : multiFileMap.entrySet()) {
            String name = entry.getKey();

            StringBuilder sb = new StringBuilder();
            List<MultipartFile> value = entry.getValue();
            for (MultipartFile file : value) {
                sb.append(md5(getBytes(file))).append('$');
            }
            sb.delete(sb.length() - 1, sb.length());

            parameterMap.put(name, new String[]{sb.toString()});
        }
    }

    private byte[] getBytes(MultipartFile value) {
        try {
            return value.getBytes();
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

    public String getJson(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) return null;

        String contentType = request.getHeader("content-type");
        if (contentType == null) return null;
        if (contentType.indexOf("application/json") < 0) return null;

        try {
            BufferedReader reader = request.getReader();
            return CharStreams.toString(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isQueryParameter(String queryString, String parameterName) {
        if (Strings.isNullOrEmpty(queryString)) return false;

        int index = queryString.indexOf(parameterName);
        if (index < 0) return false;

        if (index > 0 && queryString.charAt(index - 1) != '&') return false;

        int offset = index + parameterName.length();
        if (offset >= queryString.length()) return true;

        return queryString.charAt(offset) == '=';
    }

    private static String[] filtered = new String[]{
            "hisv", "accept-encoding", "user-agent",
            "host", "connection",
            "content-length", "content-type"
    };

    private void appendHeaders(HttpServletRequest request, StringBuilder signStr) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (ArrayUtils.contains(filtered, headerName)) continue;

            Enumeration<String> headers = request.getHeaders(headerName);
            signStr.append(headerName).append('$');

            joinEnumeration(signStr, headers);
        }
    }

    private void joinEnumeration(StringBuilder signStr, Enumeration<String> headers) {
        while (headers.hasMoreElements()) {
            signStr.append(headers.nextElement()).append('$');
        }
    }

    private void appendMethodAndUrl(HttpServletRequest request, StringBuilder signStr) {
        signStr.append(request.getMethod()).append('$');

        StringBuilder fullUrl = new StringBuilder(request.getRequestURL());
        String queryString = request.getQueryString();
        if (!Strings.isNullOrEmpty(queryString)) fullUrl.append('?').append(queryString);

        signStr.append(fullUrl).append('$');
    }

}
