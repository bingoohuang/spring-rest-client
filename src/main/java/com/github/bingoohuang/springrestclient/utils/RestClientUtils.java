package com.github.bingoohuang.springrestclient.utils;

import com.mashape.unirest.http.HttpResponse;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class RestClientUtils {

    public <T> T nullOrBody(HttpResponse<T> response) {
        val returnNull = response.header("returnNull");
        if ("true".equals(returnNull)) return null;

        return response.getBody();
    }

    public boolean isResponseJsonContentType(HttpResponse<?> response) {
        val contentType = response.header("Content-Type");
        return contentType != null && contentType.contains("application/json");
    }
}
