package com.github.bingoohuang.springrestclient.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = SpringRestClientConfig.class)
@SpringRestClientEnabledScan(basePackageClasses = SpringRestClientConfig.class)
public class SpringRestClientConfig {
}
