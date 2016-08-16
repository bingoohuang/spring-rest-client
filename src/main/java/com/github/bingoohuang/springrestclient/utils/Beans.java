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
        if (StringUtils.isEmpty(text)) return null;

        return text.startsWith("<")
            ? Xmls.unmarshal(text, clazz)
            : (text.startsWith("{")
            ? Json.unJson(text, clazz)
            : Json.unJsonArray(text, clazz));
    }

    public Object unmarshal(String text, Type returnType) {
        return JSON.parseObject(text, returnType);
    }

}
