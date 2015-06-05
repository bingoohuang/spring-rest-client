package com.github.bingoohuang.springrestclient.boot.controller;

import com.github.bingoohuang.springrestclient.boot.domain.Account;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nullController")
public class NullController {
    @RequestMapping("/returnNull")
    public Account nullAccount(@RequestBody Account account) {
        return null;
    }

    @RequestMapping("/returnEmptyString")
    public String emptyString(@RequestBody Account account) {
        Account a = new Account(100, "java");
        if (a == account) return "";
        return "error";
    }
}
