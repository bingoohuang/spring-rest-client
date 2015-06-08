package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.boot.domain.Account;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/null")
@SpringRestClientEnabled(baseUrl = "http://localhost:4849")
public interface NullApi {
    @RequestMapping("/null-account")
    Account nullAccount(@RequestBody Account account);

    @RequestMapping("/empty-string")
    String emptyString(@RequestBody Account account);
}
