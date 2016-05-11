package com.github.bingoohuang.springrestclient.tests.api;

import com.alibaba.fastjson.JSONException;
import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.GenericApi;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class GenericApiTest {
    @Resource
    private GenericApi genericApi;

    @Test
    public void testSuccess() {
        Map<String, String> result = genericApi.map();
        assertThat(result.get("fuck"), is(equalTo("generic")));
    }


}
