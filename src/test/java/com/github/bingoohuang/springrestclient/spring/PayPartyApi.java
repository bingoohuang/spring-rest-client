package com.github.bingoohuang.springrestclient.spring;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.boot.domain.PayParty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringRestClientEnabled
public interface PayPartyApi {
    @RequestMapping("/party/{sellerId}/{buyerId}")
    PayParty party(@PathVariable("sellerId") String sellerId,
                   @PathVariable("buyerId") String buyerId,
                   @RequestParam("partyId") String partyId,
                   @RequestParam("name") String name);
}
