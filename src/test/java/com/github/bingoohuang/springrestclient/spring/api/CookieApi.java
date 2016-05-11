package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringRestClientEnabled(baseUrl = "http://localhost:4849")
public interface CookieApi {
    @RequestMapping("/cookie/hello")
    String hello(@CookieValue("foo") String fooCookie);
}
