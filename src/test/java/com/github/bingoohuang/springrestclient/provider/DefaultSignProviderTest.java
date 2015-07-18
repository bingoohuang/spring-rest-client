package com.github.bingoohuang.springrestclient.provider;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DefaultSignProviderTest {
    @Test
    public void testCreateProxy() throws Exception {
        final StringBuilder signStr = new StringBuilder();
        final StringBuilder logStr = new StringBuilder();
        Appendable proxy = new AbbreviateAppendable(logStr, signStr);

        proxy.append('$');
        proxy.append("1234567890abcdefg");
        proxy.append("hello");

        assertThat(logStr.toString(), is(equalTo("$1234567...hello")));
        assertThat(signStr.toString(), is(equalTo("$1234567890abcdefghello")));
    }
}