package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.asmvalidator.annotations.*;
import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.provider.DefaultSignProvider;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@SpringRestClientEnabled(baseUrl = "http://localhost:4849",
        signProvider = DefaultSignProvider.class, createClassFileForDiagnose = true)
public interface GetCodeByMobileApi {
    @AsmValid
    @RequestMapping(value = "/getCode/{mobile}", method = GET)
    String getCode(@PathVariable("mobile") @AsmMobile String mobile);

    @AsmValid
    @RequestMapping(value = "/update-user-mobile", method = POST)
    boolean updateUserMobile(
            @RequestParam("userId")
            @AsmMinSize(18) @AsmMaxSize(19) @AsmDigits
            @AsmMessage("必须为18位或者19位数字")
            String userId,
            @RequestParam("mobile") @AsmMobile
            String mobile);
}