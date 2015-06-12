package com.github.bingoohuang.springrestclient.boot.controller;

import com.github.bingoohuang.springrestclient.boot.domain.Account;
import com.github.bingoohuang.springrestclient.boot.domain.PayParty;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("pay-party")
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

    @RequestMapping(value = "/add-party", method = POST)
    public int addParty(@RequestBody PayParty payParty) {
        return 100;
    }

    @RequestMapping(value = "/add-party2", method = POST)
    public boolean addParty2(@RequestBody PayParty payParty) {
        return true;
    }

    @RequestMapping(value = "/add-party3/{sellerId}/{buyerId}", method = POST)
    public int addParty3(@PathVariable("sellerId") String sellerId,
                         @PathVariable("buyerId") String buyerId,
                         @RequestBody PayParty payParty,
                         @RequestParam("partyId") String partyId,
                         @RequestParam("name") String name) {
        return 300;
    }

    @RequestMapping(value = "/transfer", method = POST)
    public Account transfer(@RequestBody Account fromAccount,
                            @RequestParam("sendConfirmationSms") boolean sendConfirmationSms) {
        return new Account(1234, "bingoo");
    }

    @RequestMapping(value = "/get-str", method = POST)
    public String getStr(@RequestParam("sellerId") String sellerId) {
        return sellerId;
    }

    @RequestMapping(value = "/return-void")
    public void returnVoid(@RequestParam("sellerId") String sellerId, HttpServletResponse resp) {
        resp.addHeader("sellerId", sellerId + "abc");
    }

    @RequestMapping(value = "/transfer-int", method = POST)
    public Account transferInt(@RequestBody Account account,
                               @RequestParam("msg") int msg) {
        return new Account(1234, "bingoo");
    }

    @RequestMapping(value = "/transfer-double", method = POST)
    public Account transferDouble(@RequestBody Account account,
                                  @RequestParam("msg") double msg) {
        return new Account(1234, "bingoo");
    }

    @RequestMapping(value = "/transfer-double2", method = POST)
    public Account transferDouble2(@RequestParam("msg") double ms,
                                   @RequestBody Account account) {
        return new Account(1234, "bingoo");
    }
}
