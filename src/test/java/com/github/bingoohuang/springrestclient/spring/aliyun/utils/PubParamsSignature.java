package com.github.bingoohuang.springrestclient.spring.aliyun.utils;

import com.github.bingoohuang.springrestclient.ext.ParameterDelayable;
import lombok.SneakyThrows;
import lombok.val;
import org.n3r.diamond.client.Miner;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

@Component
public class PubParamsSignature implements ParameterDelayable {
    public static final String HMAC_SHA1 = "HmacSHA1";

    @Override
    public String computeDelayedParam(Map<String, Object> mergedRequestParams) {
        val sortedMap = new TreeMap<String, Object>(mergedRequestParams);

        val sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : sortedMap.entrySet()) {
            sb.append(urlEncode(entry.getKey())).append("=")
                .append(urlEncode("" + entry.getValue())).append("&");
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 1); // remove last &
        val canonicalizedQueryString = sb.toString();

        val stringToSign = percentEncode("GET&/&") + urlEncode(canonicalizedQueryString);
        val hamcKey = getAccessSecret() + "&";
        val signature = hmacSHA1(stringToSign, hamcKey);

        return signature;
    }

    @SneakyThrows
    public String hmacSHA1(String data, String key) {
        val keySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA1);
        val mac = Mac.getInstance(HMAC_SHA1);
        mac.init(keySpec);
        val bytes = mac.doFinal(data.getBytes());

        return DatatypeConverter.printBase64Binary(bytes);
    }

    private String getAccessSecret() {
        val miner = new Miner().getMiner("aliyun", "rds");
        return miner.getString("access_secret");
    }

    private String percentEncode(String str) {
        return str.replace("/", "%2F");
    }

    @SneakyThrows
    private String urlEncode(String val) {
        String encoded = URLEncoder.encode(val, "UTF-8");
        return encoded
            .replace("+", "%20")
            .replace("*", "%2A")
            .replace("%7E", "~");
    }
}
