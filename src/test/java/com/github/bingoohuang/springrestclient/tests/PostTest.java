package com.github.bingoohuang.springrestclient.tests;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.springrestclient.boot.domain.PayParty;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

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
        assertThat(response.getBody(), is(equalTo("1")));
    }

    @Test
    public void test2() {

    }
}
