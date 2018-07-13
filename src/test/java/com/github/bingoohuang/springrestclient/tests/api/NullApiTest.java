package com.github.bingoohuang.springrestclient.tests.api;

import com.github.bingoohuang.springrestclient.boot.domain.Account;
import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.NullApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.google.common.truth.Truth.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class NullApiTest {
    @Autowired
    NullApi nullApi;

    @Test
    public void testNull() {
        Account account = new Account(100, "java");
        Account a = nullApi.nullAccount(account);
        assertThat(a).isNull();
    }

    @Test
    public void testEmptyString() {
        Account account = new Account(100, "java");
        String a = nullApi.emptyString(account);
        assertThat(a).isEmpty();
    }

}
