package com.github.bingoohuang.springrestclient.annotations;

import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;
import com.github.bingoohuang.springrestclient.provider.NoneBaseUrlProvider;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpringRestClientEnabled {
    boolean createClassFileForDiagnose() default false;

    String baseUrl() default "";

    Class<? extends BaseUrlProvider> baseUrlProvider() default NoneBaseUrlProvider.class;
}
