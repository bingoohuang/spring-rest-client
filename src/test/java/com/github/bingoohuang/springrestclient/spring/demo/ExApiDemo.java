package com.github.bingoohuang.springrestclient.spring.demo;

import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;
import com.github.bingoohuang.springrestclient.spring.api.ExApi;
import com.github.bingoohuang.springrestclient.spring.exception.NotFoundError;
import com.github.bingoohuang.springrestclient.utils.UniRests;
import com.google.common.base.Throwables;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ExApiDemo implements ExApi {
    BaseUrlProvider baseUrlProvider;
    Map<Integer, Class<? extends Throwable>> errorStatusExceptionMappings;

    @Override
    public int exception(@PathVariable("error") int error) {
        return 0;
    }

    @Override
    public int error(@PathVariable("error") int error) throws NotFoundError {
        LinkedHashMap pathVariables = new LinkedHashMap();
        LinkedHashMap requestParams = new LinkedHashMap();
        pathVariables.put("error", error);
        try {
            String value = UniRests.post(errorStatusExceptionMappings,
                    ExApi.class, baseUrlProvider, "url", pathVariables, requestParams);
            return Integer.parseInt(value);
        } catch (NotFoundError e) {
            throw e;
        } catch (Throwable throwable) {
            throw Throwables.propagate(throwable);
        }
    }
}
