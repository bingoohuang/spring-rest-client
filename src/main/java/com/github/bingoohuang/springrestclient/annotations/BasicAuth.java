package com.github.bingoohuang.springrestclient.annotations;

import com.github.bingoohuang.springrestclient.provider.BasicAuthProvider;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2016/11/25.
 */
@Documented @Target({TYPE}) @Retention(RUNTIME) public @interface BasicAuth {
    String username() default "";

    String password() default "";

    Class<? extends BasicAuthProvider> basicAuthProvider() default BasicAuthProvider.class;
}
