package com.github.bingoohuang.springrestclient.tests.api;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.springrestclient.boot.domain.Account;
import com.github.bingoohuang.springrestclient.spring.api.NullApi;
import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class NullApiTest {
    @Autowired
    NullApi nullApi;

    @Test
    public void testNull() {
        Account account = new Account(100, "java");
        Account a = nullApi.nullAccount(account);
        assertThat(a, is(nullValue()));
    }

    @Test
    public void testEmptyString() {
        Account account = new Account(100, "java");
        String a = nullApi.emptyString(account);
        assertThat(a, is(equalTo("")));
    }

    @Test
    public void testNullPost() throws UnirestException {
        Account account = new Account(100, "java");
        String json = JSON.toJSONString(account);
        HttpResponse<String> response = Unirest.post("http://localhost:4849/nullController/returnNull")
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(json)
                .asString();

        assertThat(response.getBody().length(), is(0));
        assertThat(response.getHeaders().getFirst("returnNull"), is(equalTo("true")));
    }


    @Test
    public void testEmptyPost() throws UnirestException {
        Account account = new Account(100, "java");
        String json = JSON.toJSONString(account);
        HttpResponse<String> response = Unirest.post("http://localhost:4849/nullController/returnEmptyString")
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(json)
                .asString();

        assertThat(response.getBody().length(), is(0));
        assertThat(response.getHeaders().getFirst("returnNull"), is(nullValue()));
    }
}
