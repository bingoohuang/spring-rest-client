package com.github.bingoohuang.springrestclient.boot.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import lombok.val;
import org.joda.time.DateTime;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class FastJSONMessageConverter extends FastJsonHttpMessageConverter {
    private static final ExtraProcessor ET_EXTRA_KEY_PROCESSOR = (object, key, value) -> {
        throw new RuntimeException("非法参数：" + key);
    };
    public static final String COM_RAIYEE_PROMOTION = "com.raiyee.promotion.";
    private final FastJsonConfig fastJsonConfig;

    public FastJSONMessageConverter() {
        setSupportedMediaTypes(Lists.newArrayList(MediaType.APPLICATION_JSON));
        setDefaultCharset(Charsets.UTF_8);

        this.fastJsonConfig = getFastJsonConfig();

        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue);

        ValueFilter filter = (o, s, v) -> v == null && o.getClass().getPackage().getName().startsWith(COM_RAIYEE_PROMOTION) ? "" : v;
        fastJsonConfig.setSerializeFilters(filter);

        fastJsonConfig.getSerializeConfig().put(DateTime.class, new JsonJodaSerializer("yyyy-MM-dd HH:mm:ss", true));
        fastJsonConfig.getParserConfig().putDeserializer(DateTime.class, new JsonJodaDeserializer());
    }

    @Override
    public Object read(Type type, //
                       Class<?> contextClass, //
                       HttpInputMessage inputMessage //
    ) throws IOException {
        return parseObject(inputMessage, type);
    }

    @Override
    protected Object readInternal(Class<?> clazz, //
                                  HttpInputMessage inputMessage //
    ) throws IOException {
        return parseObject(inputMessage, clazz);
    }

    private Object parseObject(HttpInputMessage inputMessage, Type clazz)
            throws IOException {
        val in = inputMessage.getBody();
        val json = new String(ByteStreams.toByteArray(in), Charsets.UTF_8);


        return tryValidate(json, clazz);
    }

    private Object tryValidate(String json, Type clazz) {
        val packageName = parsePackageName(clazz);

        val processor = packageName.startsWith(COM_RAIYEE_PROMOTION) ? null : ET_EXTRA_KEY_PROCESSOR;
        try {
            val object = JSON.parseObject(json, clazz, fastJsonConfig.getParserConfig(), processor, JSON.DEFAULT_PARSER_FEATURE);

            return object;
        } catch (Exception ex) {
            return json;
        }
    }

    private String parsePackageName(Type clazz) {
        if (clazz instanceof ParameterizedType) {
            val pt = (ParameterizedType) clazz;
            if (pt.getRawType() == List.class) {
                return ((Class) pt.getActualTypeArguments()[0]).getPackage().getName();
            }
        } else if (clazz instanceof Class) {
            return ((Class) clazz).getPackage().getName();
        }

        throw new RuntimeException("unknown type " + clazz);
    }
}
