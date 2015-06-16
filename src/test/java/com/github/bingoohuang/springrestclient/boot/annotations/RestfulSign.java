package com.github.bingoohuang.springrestclient.boot.annotations;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RestfulSign {
    boolean ignore() default false;
}
