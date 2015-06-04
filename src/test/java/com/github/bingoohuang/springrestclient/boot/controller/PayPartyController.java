package com.github.bingoohuang.springrestclient.boot.controller;

import com.github.bingoohuang.springrestclient.boot.domain.Account;
import com.github.bingoohuang.springrestclient.boot.domain.Customer;
import com.github.bingoohuang.springrestclient.boot.domain.PayParty;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
class PayPartyController {
    @RequestMapping("/party/{sellerId}/{buyerId}")
    public PayParty party(@PathVariable("sellerId") String sellerId,
                          @PathVariable("buyerId") String buyerId,
                          @RequestParam("partyId") String partyId,
                          @RequestParam("name") String name) {
        PayParty payParty = new PayParty();
        payParty.setPartyId(partyId);
        payParty.setSellerId(sellerId);
        payParty.setBuyerId(buyerId);
        payParty.setPartyName(name);

        return payParty;
    }

    @RequestMapping(value = "/addParty", method = RequestMethod.POST)
    public int addParty(@RequestBody PayParty payParty) {
        return 100;
    }

    @RequestMapping(value = "/addParty2", method = RequestMethod.POST)
    public boolean addParty2(@RequestBody PayParty payParty) {
        return true;
    }

    @RequestMapping(value = "/addParty/{sellerId}/{buyerId}", method = RequestMethod.POST)
    public int addParty3(@PathVariable("sellerId") String sellerId,
                         @PathVariable("buyerId") String buyerId,
                         @RequestBody PayParty payParty,
                         @RequestParam("partyId") String partyId,
                         @RequestParam("name") String name) {
        return 300;
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public Account transfer(@RequestBody Account fromAccount,
                            @RequestParam("sendConfirmationSms") boolean sendConfirmationSms) {
        return new Account(1234, "bingoo");
    }
    @RequestMapping(value = "/getStr", method = RequestMethod.POST)
    public String transfer(@RequestParam("sellerId") String sellerId) {
        return sellerId;
    }

    @RequestMapping(value = "/returnVoid")
    public void returnVoid(@RequestParam("sellerId") String sellerId, HttpServletResponse resp){
        resp.addHeader("sellerId", sellerId + "abc");
    }
}
