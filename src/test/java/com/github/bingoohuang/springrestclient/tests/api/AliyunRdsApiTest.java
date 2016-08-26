package com.github.bingoohuang.springrestclient.tests.api;

import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.AliyunRdsApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class AliyunRdsApiTest {
    @Autowired AliyunRdsApi aliyunRdsApi;

    @Test
    public void test() {
        aliyunRdsApi.describeBinlogFiles("2016-08-25T21:00:51Z", "2016-08-27T03:01:09Z");
    }
}
