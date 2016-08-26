package com.github.bingoohuang.springrestclient.provider;


import lombok.val;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;


public class DefaultSignProviderTest {
    @Test
    public void testCreateProxy() throws Exception {
        val signStr = new StringBuilder();
        val logStr = new StringBuilder();
        val proxy = new AbbreviateAppendable(logStr, signStr);

        proxy.append('$');
        proxy.append("1234567890abcdefg");
        proxy.append("hello");

        assertThat(logStr.toString()).isEqualTo("$1234567...hello");
        assertThat(signStr.toString()).isEqualTo("$1234567890abcdefghello");
    }
}