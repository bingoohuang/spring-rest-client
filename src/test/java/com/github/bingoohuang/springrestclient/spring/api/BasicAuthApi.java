package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.BasicAuth;
import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2016/11/25.
 */
@RequestMapping("/auth")
@SpringRestClientEnabled(baseUrl = "http://localhost:4849")
@BasicAuth(username = "username", password = "password123")
public interface BasicAuthApi {
    @RequestMapping("/hello") String hello();

    @RequestMapping("/world") String world();
}
