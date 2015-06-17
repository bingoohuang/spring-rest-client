package com.github.bingoohuang.springrestclient.tests.api;

import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.NotRequiredApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class NotRequiredApiTest {
    @Autowired
    NotRequiredApi notRequiredApi;

    @Test
    public void test1() {
        String bingooBefore = "bingoo" + System.currentTimeMillis();
        String bingoo = notRequiredApi.test1("bingoo");
        String bingooAfter = "bingoo" + System.currentTimeMillis();
        assertTrue(bingooBefore.compareTo(bingoo) < 0);
        assertTrue(bingoo.compareTo(bingooAfter) < 0);
    }

    @Test
    public void test2() {
        String bingooBefore = "" + System.currentTimeMillis();
        String bingoo = notRequiredApi.test1(null);
        String bingooAfter = "" + System.currentTimeMillis();
        assertTrue(bingooBefore.compareTo(bingoo) < 0);
        assertTrue(bingoo.compareTo(bingooAfter) < 0);
    }


    @Test
    public void test3() {
        String bingooBefore = "null" + System.currentTimeMillis();
        String bingoo = notRequiredApi.test2();
        String bingooAfter = "null" + System.currentTimeMillis();
        assertTrue(bingooBefore.compareTo(bingoo) < 0);
        assertTrue(bingoo.compareTo(bingooAfter) < 0);
    }
}
