package com.github.bingoohuang.springrestclient.provider;

import org.apache.commons.lang3.StringUtils;

class AbbreviateAppendable implements Appendable {
    private final StringBuilder logStr;
    private final StringBuilder signStr;

    public AbbreviateAppendable(StringBuilder logStr, StringBuilder signStr) {
        this.logStr = logStr;
        this.signStr = signStr;
    }

    @Override
    public Appendable append(String str) {
        logStr.append(str);
        signStr.append(str);
        return this;
    }

    @Override
    public Appendable appendAbbreviate(String str) {
        logStr.append(StringUtils.abbreviate(str, 100));
        signStr.append(str);
        return this;
    }

    @Override
    public Appendable append(char ch) {
        logStr.append(ch);
        signStr.append(ch);
        return this;
    }
}
