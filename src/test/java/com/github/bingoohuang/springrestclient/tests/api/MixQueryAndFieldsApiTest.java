package com.github.bingoohuang.springrestclient.tests.api;

import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.MixQueryAndFieldsApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class MixQueryAndFieldsApiTest {
    @Autowired
    MixQueryAndFieldsApi api;

    @Test
    public void test1() {
        String joined = api.case1("bingoo", "nanjing", 101);
        assertThat(joined, is(equalTo("bingoo,nanjing,101")));
    }
}
