package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.CheckResponseOKByJSONProperty;
import com.github.bingoohuang.springrestclient.annotations.RequestParamValue;
import com.github.bingoohuang.springrestclient.annotations.RequestParamValues;
import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringRestClientEnabled(baseUrl = "http://yunpian.com/v1")
@RequestMapping("/sms")
public interface YunpianApi {

    @RequestParamValue(name = "apikey", value = "xxx")
    @RequestMapping("/send.json")
    @CheckResponseOKByJSONProperty(key = "code", value = "0")
    void send(@RequestParam("text") String text,
              @RequestParam("mobile") String mobile);

    @RequestParamValues({
            @RequestParamValue(name = "apikey", value = "xxx")
    })
    @RequestMapping("/send.json")
    @CheckResponseOKByJSONProperty(key = "code", value = "0")
    void send2(@RequestParam("text") String text,
               @RequestParam("mobile") String mobile);
}
