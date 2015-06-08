package com.github.bingoohuang.springrestclient.tests.api;

import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.YunpianApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class YunpianTest {
    @Autowired
    YunpianApi yunpianApi;

    @Test
    public void test1() {
        yunpianApi.send("【健康生活】您正在注册验证easy-hi，验证码为123456（5分钟内有效），如非本人操作，请忽略本短信", "18551855099");
        yunpianApi.send2("【健康生活】您正在注册验证easy-hi，验证码为123456（5分钟内有效），如非本人操作，请忽略本短信", "15951770693");
    }
}
