package com.github.bingoohuang.springrestclient.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FixedRequestParams {
    FixedRequestParam[] value();
}
