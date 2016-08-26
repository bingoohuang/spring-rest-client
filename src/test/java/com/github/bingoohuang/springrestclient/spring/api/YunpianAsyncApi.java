package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.FixedRequestParam;
import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.mashape.unirest.http.HttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.Future;

@SpringRestClientEnabled(baseUrl = "http://yunpian.com/v1")
@RequestMapping("/sms/send.json")
@FixedRequestParam(name = "apikey", value = "xxx")
public interface YunpianAsyncApi {
    Future<String> sendAsync(@RequestParam("text") String text,
                             @RequestParam("mobile") String mobile);

    Future<Void> sendAsyncVoid(@RequestParam("text") String text,
                               @RequestParam("mobile") String mobile);


    Future<YunpianResult> sendAsyncResult(@RequestParam("text") String text,
                                          @RequestParam("mobile")
                                              String mobile);

    Future<HttpResponse<String>> sendAsyncResponse(
        @RequestParam("text") String text,
        @RequestParam("mobile") String mobile);
}
