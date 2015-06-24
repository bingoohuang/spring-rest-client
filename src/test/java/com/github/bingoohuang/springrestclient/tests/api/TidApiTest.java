package com.github.bingoohuang.springrestclient.tests.api;

import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.EasyHiTid;
import com.github.bingoohuang.springrestclient.spring.api.TidApi;
import com.github.bingoohuang.springrestclient.spring.mock.MockTidApi;
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
public class TidApiTest {
    @Autowired
    MockTidApi mockTidApi;
    @Autowired
    TidApi tidApi;

    @Test
    public void getMobile() {
        EasyHiTid.tid.set("1000");

        String mobile = tidApi.getMobile();
        assertThat(mobile, is(equalTo("bingoo:1000")));
    }

    @Test
    public void getMobile2() {
        mockTidApi.setMockedValue("bingoo:1000");

        String mobile = tidApi.getMobile2();
        assertThat(mobile, is(equalTo("bingoo:1000")));

        mockTidApi.setMockedValue("bingoo:2000");
        mobile = tidApi.getMobile2();
        assertThat(mobile, is(equalTo("bingoo:1000")));
    }
}
