package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/upload")
@SpringRestClientEnabled(baseUrl = "http://localhost:4849")
public interface UploadApi {
    @RequestMapping(value = "/image", method = POST)
    void image(@RequestParam("name") String name, @RequestParam("file") File file);

    @RequestMapping(value = "/images", method = POST)
    void images(@RequestParam("name") String name, @RequestParam("files") List<File> files);
}
