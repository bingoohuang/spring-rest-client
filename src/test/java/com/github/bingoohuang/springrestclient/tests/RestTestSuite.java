package com.github.bingoohuang.springrestclient.tests;

import com.github.bingoohuang.springrestclient.boot.Application;
import com.github.bingoohuang.springrestclient.tests.api.*;
import com.mashape.unirest.http.Unirest;
import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.n3r.diamond.client.impl.MockDiamondServer;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AnotherApiTest.class,
        PayPartyApiTest.class,
        ExApiTest.class,
        NullApiTest.class,
        YunpianTest.class,
        YunpianAsyncTest.class,
        GetApiTest.class,
        UploadApiTest.class,
        NotRequiredApiTest.class,
        EnumApiTest.class,
        DownloadApiTest.class,
        JsonRequestParamApiTest.class,
})
public class RestTestSuite {
    @ClassRule
    public static ExternalResource testRule = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
//            Unirest.setProxy(new org.apache.http.HttpHost("localhost", 9999));
            Application.startup();

            MockDiamondServer.setUpMockServer();
            MockDiamondServer.setConfigInfo("api", "base.urls", "AnotherApi=http://localhost:4849");
        }

        @Override
        protected void after() {
            try {
                Unirest.shutdown();
            } catch (Exception e) {
            }
            Application.shutdown();

            MockDiamondServer.tearDownMockServer();
        }
    };
}
