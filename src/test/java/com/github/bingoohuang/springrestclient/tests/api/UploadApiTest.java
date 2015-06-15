package com.github.bingoohuang.springrestclient.tests.api;

import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.UploadApi;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
}
