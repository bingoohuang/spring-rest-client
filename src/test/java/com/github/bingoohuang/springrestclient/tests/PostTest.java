package com.github.bingoohuang.springrestclient.tests;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.springrestclient.boot.domain.Account;
import com.github.bingoohuang.springrestclient.boot.domain.Customer;
import com.github.bingoohuang.springrestclient.boot.domain.PayParty;
import com.google.common.collect.Maps;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PostTest {
    @Test
    public void test1() throws UnirestException {
        PayParty payParty = new PayParty("s100", "b200", "p300", "n400");
        String json = JSON.toJSONString(payParty);

        HttpResponse<String> response = Unirest.post("http://localhost:4849/addParty")
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(json)
                .asString();
        assertThat(response.getBody(), is(equalTo("100")));
    }

    @Test
    public void test2() throws UnirestException {
        Account fromAccount = new Account(100, "from");
        String json = JSON.toJSONString(fromAccount);
        HttpResponse<String> response = Unirest.post("http://localhost:4849/transfer")
                .header("Content-Type", "application/json;charset=UTF-8")
                .queryString("sendConfirmationSms", "true")
                .body(json)
                .asString();

        Account account = JSON.parseObject(response.getBody(), Account.class);
        assertThat(account, is(equalTo(new Account(1234, "bingoo"))));
    }



    @Test
    public void test3() throws UnirestException {
        String sellerId = "中华";
        String json = JSON.toJSONString(sellerId);
        HttpResponse<String> response = Unirest.post("http://localhost:4849/getStr")
                .header("Content-Type", "application/json;charset=UTF-8")
                .queryString("sellerId", "中华")
                .body(json)
                .asString();
        String str = response.getBody();
        assertThat(sellerId, is(equalTo(str)));
    }

    @Test
    public void test4() throws UnirestException {
        String sellerId = "123456";
        HttpResponse<String> response = Unirest.post("http://localhost:4849/returnVoid")
                .queryString("sellerId", sellerId)
                .asString();
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getHeaders().getFirst("sellerId"), is(equalTo("123456abc")));
    }

    @Test
    public void test5() throws UnirestException {
        Account fromAccount = new Account(1234, "bingoo");
        String json = JSON.toJSONString(fromAccount);
        HttpResponse<String> response = Unirest.post("http://localhost:4849/transferInt")
                .header("Content-Type", "application/json;charset=UTF-8")
                .queryString("msg", 100)
                .body(json)
                .asString();

        Account account = JSON.parseObject(response.getBody(), Account.class);
        assertThat(account, is(equalTo(new Account(1234, "bingoo"))));
    }

    @Test
    public void test6() throws UnirestException {
        Account fromAccount = new Account(1234, "bingoo");
        String json = JSON.toJSONString(fromAccount);
        HttpResponse<String> response = Unirest.post("http://localhost:4849/transferDouble")
                .header("Content-Type", "application/json;charset=UTF-8")
                .queryString("msg", 100.12)
                .body(json)
                .asString();

        Account account = JSON.parseObject(response.getBody(), Account.class);
        assertThat(account, is(equalTo(new Account(1234, "bingoo"))));
    }

    @Test
    public void test7() throws UnirestException{
        int offset = 123;
        HttpResponse<String> response = Unirest.post("http://localhost:4849/another/add")
                .header("Content-Type", "application/json;charset=UTF-8")
                .queryString("offset", offset)
                .asString();

        Integer account = JSON.parseObject(response.getBody(), Integer.class);
        assertThat(account, is(equalTo(123)));
    }
}
