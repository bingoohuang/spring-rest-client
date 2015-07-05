package com.github.bingoohuang.springrestclient.spring;

import com.github.bingoohuang.springrediscache.RedisCacheSpringConfig;
import com.github.bingoohuang.utils.redis.Redis;
import com.github.bingoohuang.utils.redis.RedisConfig;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@SpringRestClientEnabledScan
@Import(RedisCacheSpringConfig.class)
public class SpringRestClientConfig {
    @Bean
    public Redis redis() {
        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setHost("127.0.0.1");
        redisConfig.setPort(6379);
        return new Redis(redisConfig);
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
}
