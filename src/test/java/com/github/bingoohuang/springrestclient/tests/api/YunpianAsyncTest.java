package com.github.bingoohuang.springrestclient.tests.api;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.YunpianAsyncApi;
import com.github.bingoohuang.springrestclient.spring.api.YunpianResult;
import com.mashape.unirest.http.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class YunpianAsyncTest {
    @Autowired
    YunpianAsyncApi asyncApi;

    @Test
    public void sendAsync() throws ExecutionException, InterruptedException {
        Future<String> future = asyncApi.sendAsync("【健康生活】您正在注册验证easy-hi，验证码为123456（5分钟内有效），如非本人操作，请忽略本短信",
                "18551855099");
        String s = future.get();
        YunpianResult result = JSON.parseObject(s, YunpianResult.class);
        assertThat(result.getCode(), is(equalTo(-1)));
        assertThat(result.getMsg(), is(equalTo("非法的apikey")));
        assertThat(result.getDetail(), is(equalTo("请检查您的apikey是否正确，或者账户已经失效")));
    }

    @Test
    public void sendAsyncVoid() throws ExecutionException, InterruptedException {
        asyncApi.sendAsyncVoid("【健康生活】您正在注册验证easy-hi，验证码为123456（5分钟内有效），如非本人操作，请忽略本短信",
                "18551855099");
    }

    @Test
    public void sendAsyncResult() throws ExecutionException, InterruptedException {
        Future<YunpianResult> future = asyncApi.sendAsyncResult("【健康生活】您正在注册验证easy-hi，验证码为123456（5分钟内有效），如非本人操作，请忽略本短信",
                "18551855099");
        YunpianResult result = future.get();
        assertThat(result.getCode(), is(equalTo(-1)));
        assertThat(result.getMsg(), is(equalTo("非法的apikey")));
        assertThat(result.getDetail(), is(equalTo("请检查您的apikey是否正确，或者账户已经失效")));
    }

    @Test
    public void sendAsyncResponse() throws ExecutionException, InterruptedException {
        Future<HttpResponse<String>> future = asyncApi.sendAsyncResponse("【健康生活】您正在注册验证easy-hi，验证码为123456（5分钟内有效），如非本人操作，请忽略本短信",
                "18551855099");

        HttpResponse<String> response = future.get();
        YunpianResult result = JSON.parseObject(response.getBody(), YunpianResult.class);
        assertThat(result.getCode(), is(equalTo(-1)));
        assertThat(result.getMsg(), is(equalTo("非法的apikey")));
        assertThat(result.getDetail(), is(equalTo("请检查您的apikey是否正确，或者账户已经失效")));
    }

}
