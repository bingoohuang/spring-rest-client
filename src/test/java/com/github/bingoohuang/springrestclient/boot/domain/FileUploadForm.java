package com.github.bingoohuang.springrestclient.boot.domain;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileUploadForm {
    private List<MultipartFile> multipartFiles;

    public List<MultipartFile> getFiles() {
        return multipartFiles;
    }

    public void setFiles(List<MultipartFile> files) {
        this.multipartFiles = files;
    }
}
