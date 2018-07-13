package com.github.bingoohuang.springrestclient.utils;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.springrestclient.xml.Xmls;
import com.github.bingoohuang.utils.codec.Json;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;

@UtilityClass
public class Beans {
    public Object unmarshal(String text, Class<?> clazz) {
        if (StringUtils.isEmpty(text)) return clazz == String.class ? text : null;

        return text.startsWith("<")
                ? Xmls.unmarshal(text, clazz)
                : text.startsWith("[")

                ? Json.unJsonArray(text, clazz)
                : text.startsWith("{")

                ? Json.unJson(text, clazz)
                : specialDeal(text, clazz);
    }

    private static Object specialDeal(String text, Class<?> clazz) {
        if (clazz == String.class) return unquote(text, '"');
        return Json.unJson(text, clazz);
    }

    private static String unquote(String text, char quote) {
        int length = StringUtils.length(text);
        if (length < 2) return text;

        if (text.charAt(0) == quote && text.charAt(length - 1) == quote) return text.substring(1, length - 1);

        return text;
    }

    public Object unmarshal(String text, Type returnType) {
        return JSON.parseObject(text, returnType);
    }

}
