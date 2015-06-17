package com.github.bingoohuang.springrestclient.boot.controller;

import com.github.bingoohuang.springrestclient.boot.exception.RestException;
import com.google.common.io.Files;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/upload")
//@RestfulSign(ignore = true)
public class UploadController {
    @RequestMapping(value = "/image", method = POST)
    public void image(@RequestParam("name") String name,
                      @RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new RestException(410, "no file uploaded");

        File tempFile = File.createTempFile(name, ".temp");
        Files.write(file.getBytes(), tempFile);
        tempFile.deleteOnExit();
    }

    @RequestMapping(value = "/images", method = POST)
    public void images(@RequestParam("name") String name,
                       @RequestParam("files") List<MultipartFile> files) throws IOException {
        if (files.isEmpty()) throw new RestException(410, "no file uploaded");

        int cnt = 0;
        for (MultipartFile file : files) {
            File tempFile = File.createTempFile(name, ".temp." + (cnt++));
            Files.write(file.getBytes(), tempFile);
            tempFile.deleteOnExit();
        }

    }
}
