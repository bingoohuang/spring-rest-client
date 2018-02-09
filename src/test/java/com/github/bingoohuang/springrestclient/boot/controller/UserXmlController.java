package com.github.bingoohuang.springrestclient.boot.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/users")
public class UserXmlController {
    private Map<Long, User> userMap = new LinkedHashMap<Long, User>();

    public void saveUser(User user) {
        if (user.getId() == null) {
            user.setId((long) (userMap.size() + 1));
        }
        userMap.put(user.getId(), user);
    }

    public List<User> getAllUsers() {
        return new ArrayList<User>(userMap.values());
    }

    @RequestMapping(value = "/register", method = POST, consumes = MediaType.APPLICATION_XML_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void handleXMLPostRequest(@RequestBody User user) {
        System.out.println("saving user: " + user);
        saveUser(user);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserList handleAllUserRequest() {
        val list = new UserList();
        list.setUsers(getAllUsers());
        return list;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    @XmlRootElement
    public static class User {
        Long id;
        String name;
        String password;
        String emailAddress;
    }

    @Data
    @XmlRootElement
    public static class UserList {
        List<User> users;
    }
}
