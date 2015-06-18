package com.github.bingoohuang.springrestclient.boot.controller;

import com.github.bingoohuang.springrestclient.boot.annotations.RestfulSign;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/download")
@RestfulSign(ignore = true)
public class DownloadController {
    @RequestMapping("/image")
    public void image(HttpServletRequest request,
                      HttpServletResponse response) throws Exception {

        ClassPathResource resource = new ClassPathResource("image/xx.jpg");
        InputStream inputStream = resource.getInputStream();

        // get MIME type of the file
        ServletContext context = request.getServletContext();
        String mimeType = context.getMimeType(resource.getPath());
        if (mimeType == null) mimeType = "application/octet-stream";

        // set content attributes for the response
        response.setContentType(mimeType);
        response.setContentLength((int) resource.getFile().length());

        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                resource.getFilename());
        response.setHeader(headerKey, headerValue);

        // get output stream of the response
        OutputStream outStream = response.getOutputStream();
        IOUtils.copy(inputStream, outStream);

        inputStream.close();
        outStream.close();
    }
}
