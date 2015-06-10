package com.github.bingoohuang.springrestclient.tests.api;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.springrestclient.exception.RestException;
import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.YunpianApi;
import com.github.bingoohuang.springrestclient.spring.api.YunpianResult;
import com.mashape.unirest.http.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class YunpianTest {
    @Autowired
    YunpianApi yunpianApi;

    @Test
    public void test1() {
        try {
            yunpianApi.send("【健康生活】您正在注册验证easy-hi，验证码为123456（5分钟内有效），如非本人操作，请忽略本短信",
                    "18551855099");
            fail();
        } catch (RestException e) {
            assertThat(e.getMessage(), is(equalTo("{\"code\":-1,\"msg\":\"非法的apikey\",\"detail\":\"请检查的apikey是否正确，或者账户已经失效\"}")));
        }
    }

    @Test
    public void test2() {
        try {
            yunpianApi.send2("【健康生活】您正在注册验证easy-hi，验证码为123456（5分钟内有效），如非本人操作，请忽略本短信",
                    "15951770693");
            fail();
        } catch (RestException e) {
            assertThat(e.getMessage(), is(equalTo("{\"code\":-1,\"msg\":\"非法的apikey\",\"detail\":\"请检查的apikey是否正确，或者账户已经失效\"}")));
        }
    }

    @Test
    public void test3() {
        YunpianResult result = yunpianApi.send3(
                "【健康生活】您正在注册验证easy-hi，验证码为123456（5分钟内有效），如非本人操作，请忽略本短信",
                "15951770693");
        assertThat(result.getCode(), is(equalTo(1)));
        assertThat(result.getMsg(), is(equalTo("请求参数缺失")));
        assertThat(result.getDetail(), is(equalTo("参数 apikey 必须传入")));
    }

}
