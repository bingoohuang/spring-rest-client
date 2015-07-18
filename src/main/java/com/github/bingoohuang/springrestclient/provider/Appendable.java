package com.github.bingoohuang.springrestclient.provider;

public interface Appendable {
    Appendable append(String str);

    Appendable appendAbbreviate(String str);

    Appendable append(char ch);
}
