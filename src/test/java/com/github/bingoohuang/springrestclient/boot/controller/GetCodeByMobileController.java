package com.github.bingoohuang.springrestclient.boot.controller;

import com.github.bingoohuang.asmvalidator.annotations.AsmDigits;
import com.github.bingoohuang.asmvalidator.annotations.AsmMaxSize;
import com.github.bingoohuang.asmvalidator.annotations.AsmMinSize;
import com.github.bingoohuang.asmvalidator.annotations.AsmMobile;
import com.github.bingoohuang.springrest.boot.annotations.RestfulSign;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RestfulSign
public class GetCodeByMobileController {
    @RequestMapping(value = "/getCode/{mobile}", method = GET)
    String getCode(@PathVariable("mobile") @AsmMobile String mobile) {
        return "code:" + mobile;
    }

    @RequestMapping(value = "/update-user-mobile", method = POST)
    boolean updateUserMobile(@RequestParam("userId") @AsmMinSize(18) @AsmMaxSize(19) @AsmDigits String userId,
                             @RequestParam("mobile") @AsmMobile String mobile) {
        return true;
    }
}
