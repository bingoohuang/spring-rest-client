package com.github.bingoohuang.springrestclient.tests.api;

import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.DownloadApi;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class DownloadApiTest {
    @Autowired
    DownloadApi downloadApi;

    @Test
    public void test1() throws IOException {
        InputStream image = downloadApi.image();

//        File targetFile = new File("src/main/resources/targetFile.tmp");
//        FileUtils.copyInputStreamToFile(image, targetFile);

        ClassPathResource resource = new ClassPathResource("image/xx.jpg");
        InputStream inputStream = resource.getInputStream();
        boolean contentEquals = IOUtils.contentEquals(image, inputStream);
        assertTrue(contentEquals);
    }

    @Test
    public void test1Async() throws IOException, ExecutionException, InterruptedException {
        Future<InputStream> imageFuture = downloadApi.imageAsync();

        ClassPathResource resource = new ClassPathResource("image/xx.jpg");
        InputStream inputStream = resource.getInputStream();
        InputStream image = imageFuture.get();
        boolean contentEquals = IOUtils.contentEquals(image, inputStream);
        assertTrue(contentEquals);
    }
}
