package com.github.bingoohuang.springrestclient.utils;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class UrlDecodesTest {
    @Test
    public void test1() {
        String s = UrlDecodes.decodeQuietly("%**");
        assertThat(s).isEqualTo("%**");
    }
}
