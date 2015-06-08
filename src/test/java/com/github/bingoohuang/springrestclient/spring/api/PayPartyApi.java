package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.boot.domain.Account;
import com.github.bingoohuang.springrestclient.boot.domain.PayParty;
import com.github.bingoohuang.springrestclient.provider.PropertiesBaseUrlProvider;
import org.springframework.web.bind.annotation.*;

@SpringRestClientEnabled(baseUrlProvider = PropertiesBaseUrlProvider.class)
@RequestMapping("/pay-party")
public interface PayPartyApi {
    @RequestMapping("/party/{sellerId}/{buyerId}")
    PayParty party(@PathVariable("sellerId") String sellerId,
                   @PathVariable("buyerId") String buyerId,
                   @RequestParam("partyId") String partyId,
                   @RequestParam("name") String name);

    @RequestMapping(value = "/add-party", method = RequestMethod.POST)
    int addParty(@RequestBody PayParty payParty);

    @RequestMapping(value = "/add-party2", method = RequestMethod.POST)
    boolean addParty2(@RequestBody PayParty payParty);

    @RequestMapping(value = "/add-party3/{sellerId}/{buyerId}", method = RequestMethod.POST)
    int addParty3(@PathVariable("sellerId") String sellerId,
                  @PathVariable("buyerId") String buyerId,
                  @RequestBody PayParty payParty,
                  @RequestParam("partyId") String partyId,
                  @RequestParam("name") String name);

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    Account transfer(@RequestBody Account fromAccount,
                     @RequestParam("sendConfirmationSms") boolean sendConfirmationSms);

    @RequestMapping(value = "/get-str", method = RequestMethod.POST)
    String getStr(@RequestParam("sellerId") String sellerId);

    @RequestMapping(value = "/return-void")
    void returnVoid(@RequestParam("sellerId") String sellerId);

    @RequestMapping(value = "/transfer-int", method = RequestMethod.POST)
    Account transferInt(@RequestBody Account account, @RequestParam("msg") int msg);

    @RequestMapping(value = "/transfer-double", method = RequestMethod.POST)
    Account transferDouble(@RequestBody Account account, @RequestParam("msg") double msg);

    @RequestMapping(value = "/transfer-double2", method = RequestMethod.POST)
    Account transferDouble2(@RequestParam("msg") double ms,
                            @RequestBody Account account);
}
