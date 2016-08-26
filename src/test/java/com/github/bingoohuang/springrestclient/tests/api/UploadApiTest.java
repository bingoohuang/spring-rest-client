package com.github.bingoohuang.springrestclient.tests.api;

import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.UploadApi;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class UploadApiTest {
    @Autowired
    UploadApi uploadApi;

    @Test
    public void testOne() throws IOException {
        // Create temp file.
        File temp = File.createTempFile("myimage", ".image");
        Files.write("Hello world", temp, Charsets.UTF_8);

        uploadApi.image("bingoohuang.txt", temp);

        temp.delete();
    }


    @Test
    public void testTwo() throws IOException {
        // Create temp file.
        File temp = File.createTempFile("myimage", ".image");
        Files.write("Hello world1111", temp, Charsets.UTF_8);


        File temp2 = File.createTempFile("myimage", ".image");
        Files.write("Hello world222", temp2, Charsets.UTF_8);


        uploadApi.images("bingoohuang.txt", Lists.newArrayList(temp, temp2));

        temp.delete();
        temp2.delete();
    }

    @Test
    public void testOne2() throws IOException {
        MockMultipartFile file = new MockMultipartFile("myimage.image",
            "/originial/name", null, "Hello test one2".getBytes("UTF-8"));
        uploadApi.image2("bingoohuang.txt", file);
    }


    @Test
    public void testTwo2() throws IOException {
        MockMultipartFile file1 = new MockMultipartFile("myimage1.image",
            "/originial/name1", null, "Hello test one1111".getBytes("UTF-8"));

        MockMultipartFile file2 = new MockMultipartFile("myimage2.image",
            "/originial/name2", null, "Hello test one22222".getBytes("UTF-8"));


        uploadApi.images2("bingoohuang.txt", Lists.<MultipartFile>newArrayList(file1, file2));
    }
}
