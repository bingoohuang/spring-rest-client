package com.github.bingoohuang.springrestclient.boot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class Account {
    int money;
    String name;
}
