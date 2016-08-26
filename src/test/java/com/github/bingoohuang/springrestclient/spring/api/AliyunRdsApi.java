package com.github.bingoohuang.springrestclient.spring.api;


import com.github.bingoohuang.springrestclient.annotations.FixedRequestParam;
import com.github.bingoohuang.springrestclient.annotations.FixedRequestParams;
import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.spring.aliyun.utils.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@SpringRestClientEnabled(baseUrl = "https://rds.aliyuncs.com/")
@FixedRequestParams({
    @FixedRequestParam(name = "Format", value = "JSON"),
    @FixedRequestParam(name = "Version", value = "2014-08-15"),
    @FixedRequestParam(name = "SignatureMethod", value = "HMAC-SHA1"),
    @FixedRequestParam(name = "SignatureNonce", clazz = PubParamsSignatureNonce.class),
    @FixedRequestParam(name = "SignatureVersion", value = "1.0"),
    @FixedRequestParam(name = "Signature", clazz = PubParamsSignature.class),
    @FixedRequestParam(name = "AccessKeyId", clazz = PubParamsAccessKeyId.class),
    @FixedRequestParam(name = "Timestamp", clazz = PubParamTimestamp.class),

    @FixedRequestParam(name = "DBInstanceId", clazz = PubParamDBInstanceId.class)
})
public interface AliyunRdsApi {
    @RequestMapping(method = RequestMethod.GET)
    @FixedRequestParams({
        @FixedRequestParam(name = "Action", value = "DescribeBinlogFiles"),
        @FixedRequestParam(name = "PageSize", value = "100"),
        @FixedRequestParam(name = "PageNumber", value = "1")
    })
    String describeBinlogFiles(
        @RequestParam("StartTime") String startTime, // 2015-06-11T15:00:00Z
        @RequestParam("EndTime") String endTime // 2016-08-05T15:00:00Z
    );
}
