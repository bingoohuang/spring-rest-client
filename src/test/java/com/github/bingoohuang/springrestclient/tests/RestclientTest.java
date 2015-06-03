package com.github.bingoohuang.springrestclient.tests;

import com.github.bingoohuang.springrestclient.boot.domain.PayParty;
import com.github.bingoohuang.springrestclient.spring.PayPartyApi;
import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class RestclientTest {
    @Autowired
    PayPartyApi payPartyApi;

    @Test
    public void test() {
        PayParty party = payPartyApi.party("s100", "b200", "p300", "n400");
        assertThat(party, is(equalTo(new PayParty("s100", "b200", "p300", "n400"))));
    }
}
