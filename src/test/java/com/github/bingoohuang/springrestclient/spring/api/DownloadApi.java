package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.InputStream;

@RequestMapping("/download")
@SpringRestClientEnabled(baseUrl = "http://localhost:4849")
public interface DownloadApi {
    @RequestMapping("/image")
    InputStream image();
}
