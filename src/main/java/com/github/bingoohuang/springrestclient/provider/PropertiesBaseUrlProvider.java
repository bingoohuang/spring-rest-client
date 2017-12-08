package com.github.bingoohuang.springrestclient.provider;


import lombok.val;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class PropertiesBaseUrlProvider implements BaseUrlProvider {
    final Properties properties = new Properties();

    public PropertiesBaseUrlProvider() {
        try {
            val is = getClass().getClassLoader().getResourceAsStream("baseUrls.properties");
            properties.load(is);
        } catch (IOException e) {

        }
    }

    @Override
    public String getBaseUrl(Class<?> apiClass) {
        return properties.getProperty(apiClass.getSimpleName());
    }

}
