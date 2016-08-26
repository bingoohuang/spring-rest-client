package com.github.bingoohuang.springrestclient.spring.demo;

import com.github.bingoohuang.springrestclient.annotations.SuccInResponseJSONProperty;
import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;

import java.util.Map;

public class YunpianApiDemo {
    BaseUrlProvider baseUrlProvider;
    Map sendStatusExceptionMappings;
    Map sendRequestParamValues;
    SuccInResponseJSONProperty sendSuccInResponseJSONProperty;


//    public Future<String> send(String var1, String var2) throws Throwable {
//        LinkedHashMap<String, Object> var3 = new LinkedHashMap();
//        LinkedHashMap<String, Object> var4 = new LinkedHashMap();
//        var4.put("text", var1);
//        var4.put("mobile", var2);
//
//        RestReq restReq = new RestReqBuilder()
//                .prefix("/sms/send.json")
//                .async(false)
//                .apiClass(YunpianApi.class)
//                .baseUrlProvider(baseUrlProvider)
//                .succInResponseJSONProperty(sendSuccInResponseJSONProperty)
//                .statusExceptionMappings(sendStatusExceptionMappings)
//                .fixedRequestParams(sendRequestParamValues)
//                .routeParams(var3)
//                .requestParams(var4)
//                .build();
//
//        return Futures.convertFuture(restReq.postAsync(), String.class, restReq);
//    }
}
