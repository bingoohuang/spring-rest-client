package com.github.bingoohuang.springrestclient.boot;

import com.github.bingoohuang.springrest.boot.RestConfiguration;
import com.github.bingoohuang.springrest.boot.advisor.RequestMappingAdvisor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

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
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
}


