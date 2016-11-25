package com.github.bingoohuang.springrestclient.tests.api;

import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.BasicAuthApi;
import com.github.bingoohuang.springrestclient.spring.api.DynamicBasicAuthApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2016/11/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class BasicAuthApiTest {
    @Autowired BasicAuthApi basicAuthApi;
    @Autowired DynamicBasicAuthApi dynamicBasicAuthApi;

    @Test public void testBasic() {
        String hello = basicAuthApi.hello();
        assertThat(hello).isEqualTo("world");
        String world = basicAuthApi.world();
        assertThat(world).isEqualTo("hello");
    }

    @Test public void testDynamic() {
        String hello = dynamicBasicAuthApi.hello();
        assertThat(hello).isEqualTo("world");
        String world = dynamicBasicAuthApi.world();
        assertThat(world).isEqualTo("hello");
    }
}
