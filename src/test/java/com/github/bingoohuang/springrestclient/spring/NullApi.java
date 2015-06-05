package com.github.bingoohuang.springrestclient.spring;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.boot.domain.Account;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringRestClientEnabled(createClassFileForDiagnose = true)
@RequestMapping("/nullController")
public interface NullApi {
    @RequestMapping("/returnNull")
    Account nullAccount(@RequestBody Account account);

    @RequestMapping("/returnEmptyString")
    String emptyString(@RequestBody Account account);
}
