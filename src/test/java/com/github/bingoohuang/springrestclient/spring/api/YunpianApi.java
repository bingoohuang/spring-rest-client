package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.FixedRequestParam;
import com.github.bingoohuang.springrestclient.annotations.FixedRequestParams;
import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.annotations.SuccInResponseJSONProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringRestClientEnabled(baseUrl = "http://yunpian.com/v1")
@RequestMapping("/sms/send.json")
public interface YunpianApi {

    @FixedRequestParam(name = "apikey", value = "xxx")
    @SuccInResponseJSONProperty(key = "code", value = "0")
    void send(@RequestParam("text") String text,
              @RequestParam("mobile") String mobile);

    @FixedRequestParams({
        @FixedRequestParam(name = "apikey", value = "xxx")
    })
    @SuccInResponseJSONProperty(key = "code", value = "0")
    void send2(@RequestParam("text") String text,
               @RequestParam("mobile") String mobile);

    YunpianResult send3(@RequestParam("text") String text,
                        @RequestParam("mobile") String mobile);

}
