package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.boot.controller.UserXmlController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/users")
@SpringRestClientEnabled(baseUrl = "http://localhost:4849")
public interface UserXmlApi {
    @RequestMapping(value = "/register",
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_XML_VALUE)
    void handleXMLPostRequest(@RequestBody UserXmlController.User user);

    @RequestMapping(method = RequestMethod.GET)
    UserXmlController.UserList handleAllUserRequest();
}
