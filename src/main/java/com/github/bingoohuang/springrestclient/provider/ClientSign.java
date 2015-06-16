package com.github.bingoohuang.springrestclient.provider;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientSign {
    String clientKey();

    String security();
}
