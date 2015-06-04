package com.github.bingoohuang.springrestclient.demo;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.springrestclient.boot.domain.Account;
import com.github.bingoohuang.springrestclient.boot.domain.PayParty;
import com.github.bingoohuang.springrestclient.spring.PayPartyApi;
import com.github.bingoohuang.springrestclient.utils.UniRestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;

public class PayPartyApiDemo implements PayPartyApi {
    @Override
    public PayParty party(@PathVariable("sellerId") String sellerId, @PathVariable("buyerId") String buyerId, @RequestParam("partyId") String partyId, @RequestParam("name") String name) {
        return null;
    }

    @Override
    public int addParty(@RequestBody PayParty payParty) {
        LinkedHashMap pathVariables = new LinkedHashMap();
        LinkedHashMap requestParams = new LinkedHashMap();

        String str = UniRestUtils.postAsJson("url", pathVariables, requestParams, payParty);

        return Integer.valueOf(str);
    }

    @Override
    public boolean addParty2(@RequestBody PayParty payParty) {
        return false;
    }

    @Override
    public int addParty3(@PathVariable("sellerId") String sellerId, @PathVariable("buyerId") String buyerId, @RequestBody PayParty payParty, @RequestParam("partyId") String partyId, @RequestParam("name") String name) {
        return 0;
    }

    @Override
    public Account transfer(@RequestBody Account fromAccount, @RequestParam("sendConfirmationSms") boolean sendConfirmationSms) {
        LinkedHashMap pathVariables = new LinkedHashMap();
        LinkedHashMap requestParams = new LinkedHashMap();
        requestParams.put("sendConfirmationSms", sendConfirmationSms);

        String json = UniRestUtils.postAsJson("url", pathVariables, requestParams, fromAccount);

        return JSON.parseObject(json, Account.class);
    }

    @Override
    public String getStr(@RequestParam("sellerId") String sellerId) {
        LinkedHashMap pathVariables = new LinkedHashMap();
        LinkedHashMap requestParams = new LinkedHashMap();
        requestParams.put("sellerId",sellerId);
        return UniRestUtils.post("url",pathVariables, requestParams);
    }

    @Override
    public void returnVoid(@RequestParam("sellerId") String sellerId) {
        LinkedHashMap pathVariables = new LinkedHashMap();
        LinkedHashMap requestParams = new LinkedHashMap();
        requestParams.put("sellerId",sellerId);
        UniRestUtils.post("url",pathVariables, requestParams);
    }


}
