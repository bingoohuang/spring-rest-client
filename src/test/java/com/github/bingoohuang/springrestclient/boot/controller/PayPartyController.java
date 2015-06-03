package com.github.bingoohuang.springrestclient.boot.controller;

import com.github.bingoohuang.springrestclient.boot.domain.PayParty;
import org.springframework.web.bind.annotation.*;

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
    public int addPary(@RequestBody PayParty payParty) {
        return 1;
    }
}
