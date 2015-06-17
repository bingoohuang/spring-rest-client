package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.provider.DefaultSignProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/upload")
@SpringRestClientEnabled(baseUrl = "http://localhost:4849", signProvider = DefaultSignProvider.class)
public interface UploadApi {
    @RequestMapping(value = "/image", method = POST)
    void image(@RequestParam("name") String name, @RequestParam("file") File file);

    @RequestMapping(value = "/images", method = POST)
    void images(@RequestParam("name") String name, @RequestParam("files") List<File> files);

    @RequestMapping(value = "/image", method = POST)
    void image2(@RequestParam("name") String name, @RequestParam("file") MultipartFile file);

    @RequestMapping(value = "/images", method = POST)
    void images2(@RequestParam("name") String name, @RequestParam("files") List<MultipartFile> files);
}
