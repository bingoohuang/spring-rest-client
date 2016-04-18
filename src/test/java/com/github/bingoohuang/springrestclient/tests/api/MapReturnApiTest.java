package com.github.bingoohuang.springrestclient.tests.api;

import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.MapReturnApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.truth.Truth.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class MapReturnApiTest {

    @Autowired
    MapReturnApi mapReturnApi;

    @Test
    public void showme() {
        Map<String, String> result = mapReturnApi.showme();
        assertThat(result).isEqualTo(of("name", "bingoo", "department", "ebs"));
    }
}
