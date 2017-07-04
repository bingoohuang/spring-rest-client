package com.github.bingoohuang.springrestclient.boot;

import com.github.bingoohuang.springrest.boot.RestConfiguration;
import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import lombok.val;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

@SpringBootApplication
@Import({RestConfiguration.class})
public class Application {
    static ConfigurableApplicationContext context;

    public static void startup() {
        context = SpringApplication.run(Application.class);
    }

    public static void shutdown() {
        context.close();
    }

    public static void main(String[] args) {
        startup();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        val creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public WebMvcRegistrationsAdapter webMvcRegistrationsAdapter() {
        return new WebMvcRegistrationsAdapter() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new RequestMappingHandlerMapping() {
                    @Override
                    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
                        if (method.getDeclaringClass().isAnnotationPresent(SpringRestClientEnabled.class)) {
                            return; // by pass SpringRestClientEnabled interface
                        }
                        super.registerHandlerMethod(handler, method, mapping);
                    }
                };
            }
        };
    }

}


