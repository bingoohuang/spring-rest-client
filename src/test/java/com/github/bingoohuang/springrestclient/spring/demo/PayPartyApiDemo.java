package com.github.bingoohuang.springrestclient.spring.demo;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.springrestclient.boot.domain.Account;
import com.github.bingoohuang.springrestclient.boot.domain.PayParty;
import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;
import com.github.bingoohuang.springrestclient.spring.api.PayPartyApi;
import com.github.bingoohuang.springrestclient.utils.UniRests;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;

public class PayPartyApiDemo implements PayPartyApi {
    BaseUrlProvider baseUrlProvider;

    @Override
    public PayParty party(@PathVariable("sellerId") String sellerId, @PathVariable("buyerId") String buyerId, @RequestParam("partyId") String partyId, @RequestParam("name") String name) {
        return null;
    }

    @Override
    public int addParty(@RequestBody PayParty payParty) {
        LinkedHashMap pathVariables = new LinkedHashMap();
        LinkedHashMap requestParams = new LinkedHashMap();

        String str = null;
        try {
            str = UniRests.postAsJson(null, PayPartyApi.class,
                    baseUrlProvider,
                    "url", pathVariables, requestParams, payParty);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return Integer.parseInt(str);
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

        String json = null;
        try {
            json = UniRests.postAsJson(null, PayPartyApi.class,
                    baseUrlProvider, "url", pathVariables, requestParams, fromAccount);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return JSON.parseObject(json, Account.class);
    }

    @Override
    public String getStr(@RequestParam("sellerId") String sellerId) {
        LinkedHashMap pathVariables = new LinkedHashMap();
        LinkedHashMap requestParams = new LinkedHashMap();
        requestParams.put("sellerId", sellerId);
        try {
            return UniRests.post(null, PayPartyApi.class,
                    baseUrlProvider, "url", pathVariables, requestParams);
        } catch (Throwable throwable) {
            return null;
        }
    }

    @Override
    public void returnVoid(@RequestParam("sellerId") String sellerId) {
        LinkedHashMap pathVariables = new LinkedHashMap();
        LinkedHashMap requestParams = new LinkedHashMap();
        requestParams.put("sellerId", sellerId);
        try {
            UniRests.post(null, PayPartyApi.class,
                    baseUrlProvider, "url", pathVariables, requestParams);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public Account transferInt(@RequestBody Account account, int msg) {
        LinkedHashMap pathVariables = new LinkedHashMap();
        LinkedHashMap requestParams = new LinkedHashMap();
        requestParams.put("sendConfirmationSms", msg);

        String json = null;
        try {
            json = UniRests.postAsJson(null, PayPartyApi.class,
                    baseUrlProvider, "url", pathVariables, requestParams, account);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return JSON.parseObject(json, Account.class);
    }

    @Override
    public Account transferDouble(@RequestBody Account account, @RequestParam("msg") double msg) {
        LinkedHashMap pathVariables = new LinkedHashMap();
        LinkedHashMap requestParams = new LinkedHashMap();
        requestParams.put("msg", msg);

        String json = null;
        try {
            json = UniRests.postAsJson(null, PayPartyApi.class,
                    baseUrlProvider, "url", pathVariables, requestParams, account);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return JSON.parseObject(json, Account.class);
    }

    @Override
    public Account transferDouble2(@RequestParam("msg") double msg, @RequestBody Account account) {
        LinkedHashMap pathVariables = new LinkedHashMap();
        LinkedHashMap requestParams = new LinkedHashMap();
        requestParams.put("msg", msg);

        String json = null;
        try {
            json = UniRests.postAsJson(null, PayPartyApi.class,
                    baseUrlProvider, "url", pathVariables, requestParams, account);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return JSON.parseObject(json, Account.class);
    }


}
