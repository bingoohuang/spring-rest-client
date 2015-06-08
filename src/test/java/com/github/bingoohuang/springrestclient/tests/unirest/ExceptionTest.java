package com.github.bingoohuang.springrestclient.tests.unirest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ExceptionTest {
    @Test
    public void exception1() throws UnirestException {
        HttpResponse<String> response = Unirest.get("http://localhost:4849/exception/error/1").asString();
        assertThat(response.getStatus(), is(equalTo(404)));

        response = Unirest.get("http://localhost:4849/exception/error/2").asString();
        assertThat(response.getStatus(), is(equalTo(405)));

        response = Unirest.get("http://localhost:4849/exception/error/3").asString();
        assertThat(response.getStatus(), is(equalTo(500)));

        response = Unirest.get("http://localhost:4849/exception/error/4").asString();
        assertThat(response.getStatus(), is(equalTo(406)));

        response = Unirest.get("http://localhost:4849/exception/error/5").asString();
        assertThat(response.getBody(), is(equalTo("5")));
    }
}
