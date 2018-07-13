package com.github.bingoohuang.springrestclient.boot;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.github.bingoohuang.springrest.boot.RestConfiguration;
import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.boot.util.FastJSONMessageConverter;
import com.github.bingoohuang.springrestclient.boot.util.JsonJodaSerializer;
import lombok.val;
import org.joda.time.DateTime;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
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

    /**
     * 注册fastjson的消息转换器.
     */
    @Bean
    public HttpMessageConverters httpMessageConverters() {
        SerializeConfig.getGlobalInstance().put(DateTime.class, new JsonJodaSerializer());
        return new HttpMessageConverters(new FastJSONMessageConverter());
    }

    @Bean
    public WebMvcRegistrations webMvcRegistrationsAdapter() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new RequestMappingHandlerMapping() {
                    @Override
                    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
                        // by pass SpringRestClientEnabled interface
                        if (method.getDeclaringClass().isAnnotationPresent(SpringRestClientEnabled.class)) return;
                        super.registerHandlerMethod(handler, method, mapping);
                    }
                };
            }
        };
    }
}


