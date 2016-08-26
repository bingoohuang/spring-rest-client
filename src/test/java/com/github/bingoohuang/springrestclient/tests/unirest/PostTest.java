package com.github.bingoohuang.springrestclient.tests.unirest;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.springrestclient.boot.domain.Account;
import com.github.bingoohuang.springrestclient.boot.domain.PayParty;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.MultipartBody;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PostTest {

    @Test
    public void uploadOne() throws IOException {
        // Create temp file.
        File temp = File.createTempFile("myimage", ".image");
        Files.write("Hello world", temp, Charsets.UTF_8);


        try {
            HttpRequestWithBody post = Unirest.post("http://localhost:4849/upload/image");
            post.field("name", "bingoo.txt");
            post.field("file", temp);
            HttpResponse<String> file = post
                .asString();
            System.out.println(file.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }


        temp.delete();
    }

    @Test
    public void uploadTwo() throws IOException {
        // Create temp file.
        File temp1 = File.createTempFile("myimage", ".image");
        Files.write("Hello world1111", temp1, Charsets.UTF_8);

        File temp2 = File.createTempFile("myimage", ".image");
        Files.write("Hello world2222", temp2, Charsets.UTF_8);

        File temp3 = File.createTempFile("myimage", ".image");
        Files.write("Hello 33333", temp3, Charsets.UTF_8);


        try {
            MultipartBody field = Unirest.post("http://localhost:4849/upload/images")
                .field("name", "bingoo.txt")
                .field("files", temp1)
                .field("files", temp2)
                .field("files", temp3);
            HttpResponse<String> file = field.asString();
            System.out.println(file.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }


        temp1.delete();
        temp2.delete();
    }

    @Test
    public void test1() throws UnirestException {
        PayParty payParty = new PayParty("s100", "b200", "p300", "n400");
        String json = JSON.toJSONString(payParty);

        HttpResponse<String> response = Unirest.post("http://localhost:4849/pay-party/add-party")
            .header("Content-Type", "application/json;charset=UTF-8")
            .body(json)
            .asString();
        assertThat(response.getBody(), is(equalTo("100")));
    }

    @Test
    public void test2() throws UnirestException {
        Account fromAccount = new Account(100, "from");
        String json = JSON.toJSONString(fromAccount);
        HttpResponse<String> response = Unirest.post("http://localhost:4849/pay-party/transfer")
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
        HttpResponse<String> response = Unirest.post("http://localhost:4849/pay-party/get-str")
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
        HttpResponse<String> response = Unirest.post("http://localhost:4849/pay-party/return-void")
            .queryString("sellerId", sellerId)
            .asString();
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.header("sellerId"), is(equalTo("123456abc")));
    }

    @Test
    public void test5() throws UnirestException {
        Account fromAccount = new Account(1234, "bingoo");
        String json = JSON.toJSONString(fromAccount);
        HttpResponse<String> response = Unirest.post("http://localhost:4849/pay-party/transfer-int")
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
        HttpResponse<String> response = Unirest.post("http://localhost:4849/pay-party/transfer-double")
            .header("Content-Type", "application/json;charset=UTF-8")
            .queryString("msg", 100.12)
            .body(json)
            .asString();

        Account account = JSON.parseObject(response.getBody(), Account.class);
        assertThat(account, is(equalTo(new Account(1234, "bingoo"))));
    }

    @Test
    public void test7() throws UnirestException {
        int offset = 123;
        HttpResponse<String> response = Unirest.post("http://localhost:4849/another/add")
            .header("Content-Type", "application/json;charset=UTF-8")
            .queryString("offset", offset)
            .asString();

        Integer account = JSON.parseObject(response.getBody(), Integer.class);
        assertThat(account, is(equalTo(123)));
    }

    @Test
    public void testNullPost() throws UnirestException {
        Account account = new Account(110, "java");
        String json = JSON.toJSONString(account);
        HttpResponse<String> response = Unirest.post("http://localhost:4849/null/null-account")
            .header("Content-Type", "application/json;charset=UTF-8")
            .body(json)
            .asString();

        assertThat(response.getBody().length(), is(0));
        assertThat(response.header("returnNull"), is(equalTo("true")));
    }


}
