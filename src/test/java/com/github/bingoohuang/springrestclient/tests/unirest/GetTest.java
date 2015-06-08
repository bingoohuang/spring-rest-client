package com.github.bingoohuang.springrestclient.tests.unirest;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.springrestclient.boot.domain.PayParty;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GetTest {
    @Test
    public void test1() throws UnirestException {
        HttpResponse<String> httpResponse = Unirest.get("http://localhost:4849/pay-party/party/{sellerId}/{buyerId}")
                .routeParam("sellerId", "s100")
                .routeParam("buyerId", "b200")
                .queryString("partyId", "p300")
                .queryString("name", "n400")
                .asString();

        String json = httpResponse.getBody();

        PayParty payParty = JSON.parseObject(json, PayParty.class);

        assertThat(payParty, is(equalTo(new PayParty("s100", "b200", "p300", "n400"))));
    }

    @Test
    public void test2() {
    }
}
