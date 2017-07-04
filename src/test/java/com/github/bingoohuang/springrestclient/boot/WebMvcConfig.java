package com.github.bingoohuang.springrestclient.boot;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

@Configuration
public class WebMvcConfig /*extends WebMvcConfigurationSupport*/ {
//    @Override
//    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
//        return new RequestMappingHandlerMapping() {
//            @Override
//            protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
//                if (method.getDeclaringClass().isAnnotationPresent(SpringRestClientEnabled.class)) {
//                    return; // by pass SpringRestClientEnabled interface
//                }
//                super.registerHandlerMethod(handler, method, mapping);
//            }
//        };
//    }
}
