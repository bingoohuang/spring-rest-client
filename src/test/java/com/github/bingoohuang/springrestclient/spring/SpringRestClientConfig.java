package com.github.bingoohuang.springrestclient.spring;

import com.github.bingoohuang.voucherno.JedisProxy;
import com.github.bingoohuang.westcache.spring.WestCacheableEnabled;
import lombok.val;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ComponentScan
@SpringRestClientEnabledScan
@WestCacheableEnabled
public class SpringRestClientConfig {
    /**
     * 生成JedisCommands可以注入的对象.
     *
     * @return JedisCommands
     */
    @Bean
    public Jedis jedis() {
        val poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMaxWaitMillis(1000 * 10);
        poolConfig.setTestOnBorrow(true);

        val jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6379, 2000);
        return JedisProxy.createJedisProxy(jedisPool);
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
}
