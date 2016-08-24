package com.github.bingoohuang.springrestclient.boot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class PayParty {
    private String partyId;
    private String partyName;
    private String sellerId;
    private String buyerId;
}
