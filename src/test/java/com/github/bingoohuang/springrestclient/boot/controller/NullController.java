package com.github.bingoohuang.springrestclient.boot.controller;

import com.github.bingoohuang.springrest.boot.annotations.RestfulSign;
import com.github.bingoohuang.springrestclient.boot.domain.Account;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/null")
@RestfulSign
public class NullController {
    @RequestMapping("/null-account")
    public Account nullAccount(@RequestBody Account account) {
        return null;
    }

    @RequestMapping("/empty-string")
    public String emptyString(@RequestBody Account account) {
        Account a = new Account(100, "java");
        if (a.equals(account)) return "";
        return "error";
    }
}
