package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.boot.domain.Account;
import com.github.bingoohuang.springrestclient.boot.domain.PayParty;
import com.github.bingoohuang.springrestclient.provider.DefaultSignProvider;
import com.github.bingoohuang.springrestclient.provider.PropertiesBaseUrlProvider;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@SpringRestClientEnabled(baseUrlProvider = PropertiesBaseUrlProvider.class,
        signProvider = DefaultSignProvider.class)
@RequestMapping("/pay-party")
public interface PayPartyApi {
    @RequestMapping("/party/{sellerId}/{buyerId}")
    PayParty party(@PathVariable("sellerId") String sellerId,
                   @PathVariable("buyerId") String buyerId,
                   @RequestParam("partyId") String partyId,
                   @RequestParam("name") String name);

    @RequestMapping(value = "/add-party", method = POST)
    int addParty(@RequestBody PayParty payParty);

    @RequestMapping(value = "/add-party2", method = POST)
    boolean addParty2(@RequestBody PayParty payParty);

    @RequestMapping(value = "/add-party3/{sellerId}/{buyerId}", method = POST)
    int addParty3(@PathVariable("sellerId") String sellerId,
                  @PathVariable("buyerId") String buyerId,
                  @RequestBody PayParty payParty,
                  @RequestParam("partyId") String partyId,
                  @RequestParam("name") String name);

    @RequestMapping(value = "/transfer", method = POST)
    Account transfer(@RequestBody Account fromAccount,
                     @RequestParam("sendConfirmationSms") boolean sendConfirmationSms);

    @RequestMapping(value = "/get-str", method = POST)
    String getStr(@RequestParam("sellerId") String sellerId);

    @RequestMapping(value = "/return-void")
    void returnVoid(@RequestParam("sellerId") String sellerId);

    @RequestMapping(value = "/transfer-int", method = POST)
    Account transferInt(@RequestBody Account account, @RequestParam("msg") int msg);

    @RequestMapping(value = "/transfer-double", method = POST)
    Account transferDouble(@RequestBody Account account, @RequestParam("msg") double msg);

    @RequestMapping(value = "/transfer-double2", method = POST)
    Account transferDouble2(@RequestParam("msg") double ms,
                            @RequestBody Account account);
}
