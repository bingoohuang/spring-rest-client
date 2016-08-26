package com.github.bingoohuang.springrestclient.boot.domain;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FileUploadForm {
    private List<MultipartFile> files;
}
