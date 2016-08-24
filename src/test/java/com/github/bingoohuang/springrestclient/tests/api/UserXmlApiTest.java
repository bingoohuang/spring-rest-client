package com.github.bingoohuang.springrestclient.tests.api;

import com.github.bingoohuang.springrestclient.boot.controller.UserXmlController;
import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.UserXmlApi;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class UserXmlApiTest {
    @Autowired UserXmlApi userXmlApi;

    @Test
    public void testXML() {
        val user = new UserXmlController.User(1L, "bingoo", "password", "password");
        userXmlApi.handleXMLPostRequest(user);

        val userList = userXmlApi.handleAllUserRequest();
        val users = userList.getUsers();
        assertThat(users).hasSize(1);

        val user1 = users.get(0);

        assertThat(user1).isEqualTo(user);
    }
}
