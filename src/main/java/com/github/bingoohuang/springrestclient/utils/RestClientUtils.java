package com.github.bingoohuang.springrestclient.utils;

import com.mashape.unirest.http.HttpResponse;

public class RestClientUtils {

    public static <T> T nullOrBody(HttpResponse<T> response) {
        String returnNull = response.header("returnNull");
        if ("true".equals(returnNull)) return null;

        return response.getBody();
    }

    public static boolean isResponseJsonContentType(HttpResponse<?> response) {
        String contentType = response.header("Content-Type");
        return contentType != null && contentType.contains("application/json");
    }
}
