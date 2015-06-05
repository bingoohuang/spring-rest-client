package com.github.bingoohuang.springrestclient.tests;

import com.github.bingoohuang.springrestclient.spring.AnotherApi;
import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
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
public class AnotherApiTest {

    @Autowired
    AnotherApi anotherApi;

    @Test
    public void anotherAdd() {
        int a = 123;
        int account = anotherApi.add(a);
        assertThat(account, is(equalTo(123)));
    }
}
