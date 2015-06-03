package com.github.bingoohuang.springrestclient.spring;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.boot.domain.Account;
import com.github.bingoohuang.springrestclient.boot.domain.PayParty;
import org.springframework.web.bind.annotation.*;

@SpringRestClientEnabled
public interface PayPartyApi {
    @RequestMapping("/party/{sellerId}/{buyerId}")
    PayParty party(@PathVariable("sellerId") String sellerId,
                   @PathVariable("buyerId") String buyerId,
                   @RequestParam("partyId") String partyId,
                   @RequestParam("name") String name);

    @RequestMapping(value = "/addParty", method = RequestMethod.POST)
    int addParty(@RequestBody PayParty payParty);

    @RequestMapping(value = "/addParty2", method = RequestMethod.POST)
    boolean addParty2(@RequestBody PayParty payParty);

    @RequestMapping(value = "/addParty/{sellerId}/{buyerId}", method = RequestMethod.POST)
    int addParty3(@PathVariable("sellerId") String sellerId,
                  @PathVariable("buyerId") String buyerId,
                  @RequestBody PayParty payParty,
                  @RequestParam("partyId") String partyId,
                  @RequestParam("name") String name);

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    Account transfer(@RequestBody Account fromAccount,
                            @RequestParam("sendConfirmationSms") boolean sendConfirmationSms);
}
