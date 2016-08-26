package com.github.bingoohuang.springrestclient.spring.aliyun.utils;

import lombok.val;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Component
public class PubParamTimestamp {
    @Override public String toString() {
        // 请求的时间戳。日期格式按照ISO8601标准表示，并需要使用UTC时间。
        // 格式为：YYYY-MM-DDThh:mm:ssZ;例如，2013-08-15T12:00:00Z（为北京时间2013年8月15日20点0分0秒）
        val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date());
    }
}
