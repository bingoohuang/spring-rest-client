package com.github.bingoohuang.springrestclient.tests;

import com.github.bingoohuang.springrestclient.boot.domain.Account;
import com.github.bingoohuang.springrestclient.boot.domain.PayParty;
import com.github.bingoohuang.springrestclient.spring.AnotherApi;
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

    @Autowired
    AnotherApi anotherApi;

    @Test
    public void party() {
        PayParty party = payPartyApi.party("s100", "b200", "p300", "n400");
        assertThat(party, is(equalTo(new PayParty("s100", "b200", "p300", "n400"))));
    }

    @Test
    public void addParty() {
        PayParty payParty = new PayParty("s100", "b200", "p300", "n400");
        int count = payPartyApi.addParty(payParty);

        assertThat(count, is(equalTo(100)));
    }

    @Test
    public void addParty2() {
        PayParty payParty = new PayParty("s100", "b200", "p300", "n400");
        boolean count = payPartyApi.addParty2(payParty);

        assertThat(count, is(equalTo(true)));
    }

    @Test
    public void addParty3() {
        PayParty payParty = new PayParty("s100", "b200", "p300", "n400");
        int count = payPartyApi.addParty3("s100", "b200", payParty, "p300", "n400");

        assertThat(count, is(equalTo(300)));
    }

    @Test
    public void transfer() {
        Account fromAccount = new Account(100, "from");
        Account account = payPartyApi.transfer(fromAccount, true);
        assertThat(account, is(equalTo(new Account(1234, "bingoo"))));
    }
    @Test
    public void getStr(){
        String sellerId = "中华";
        String str = payPartyApi.getStr(sellerId);
        assertThat(sellerId, is(equalTo(str)));
    }
    @Test
    public void returnVoid(){
        String sellerId = "123456";
        payPartyApi.returnVoid(sellerId);

    }

    @Test
    public void transferInt() {
        Account fromAccount = new Account(100, "from");
        Account account = payPartyApi.transferInt(fromAccount, 100);
        assertThat(account, is(equalTo(new Account(1234, "bingoo"))));
    }

    @Test
    public void transferDouble() {
        Account fromAccount = new Account(100, "from");
        Account account = payPartyApi.transferDouble(fromAccount, 100.12);
        assertThat(account, is(equalTo(new Account(1234, "bingoo"))));
    }
    @Test
    public void transferDouble2() {
        Account fromAccount = new Account(100, "from");
        Account account = payPartyApi.transferDouble2(100.12, fromAccount);
        assertThat(account, is(equalTo(new Account(1234, "bingoo"))));
    }

    @Test
    public void anotherAdd() {
        int a = 123;
        int account = anotherApi.add(a);
        assertThat(account, is(equalTo(123)));
    }
}