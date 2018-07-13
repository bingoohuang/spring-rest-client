package com.github.bingoohuang.springrestclient.tests.api;

import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.EasyHiTid;
import com.github.bingoohuang.springrestclient.spring.api.TidApi;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class TidApiTest {
    @Autowired
    TidApi tidApi;
    @Autowired
    Jedis redis;

    @Test
    public void getMobile() {
        EasyHiTid.tid.set("1000");

        String mobile = tidApi.getMobile();
        assertThat(mobile, is(equalTo("bingoo:1000")));

        mobile = tidApi.getMobile("1000");
        assertThat(mobile, is(equalTo("bingoo:1000")));
    }

    @Test
    public <T extends TidApi> void getMobile2() {
        redis.del("Cache:TidApi:Mobile:1000");

        EasyHiTid.tid.set("1000");
        new MockUp<T>() {
            @Mock
            String getMobile2() {
                return "bingoo:1000";
            }
        };

        String mobile = tidApi.getMobile2();
        assertThat(mobile, is(equalTo("bingoo:1000")));

        new MockUp<T>() {
            @Mock
            String getMobile2() {
                return "bingoo:2000";
            }
        };

        mobile = tidApi.getMobile2();
        assertThat(mobile, is(equalTo("bingoo:1000")));
    }
}
