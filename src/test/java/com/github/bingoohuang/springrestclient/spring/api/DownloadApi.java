package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.provider.DefaultSignProvider;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.InputStream;
import java.util.concurrent.Future;

@RequestMapping("/download")
@SpringRestClientEnabled(baseUrl = "http://localhost:4849", signProvider = DefaultSignProvider.class)
public interface DownloadApi {
    @RequestMapping("/image")
    InputStream image();

    @RequestMapping("/image")
    Future<InputStream> imageAsync();
}
