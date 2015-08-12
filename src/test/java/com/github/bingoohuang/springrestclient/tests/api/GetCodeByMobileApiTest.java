package com.github.bingoohuang.springrestclient.tests.api;

import com.github.bingoohuang.asmvalidator.ex.AsmValidatorException;
import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.GetCodeByMobileApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class GetCodeByMobileApiTest {
    @Autowired
    GetCodeByMobileApi api;

    @Test(expected = AsmValidatorException.class)
    public void badMobile() {
        api.getCode("1XXX");
    }

    @Test
    public void nullMobile() {
        api.getCode(null);
    }

    @Test
    public void okMobile() {
        String code = api.getCode("18602506990");
        assertThat(code, is(equalTo("code:18602506990")));
    }


    @Test
    public void updateUserMobile() {
        boolean ok = api.updateUserMobile("123456789012345678", "18602506990");
        assertThat(ok, is(true));
    }


    @Test
    public void updateUserMobileUserIdTooLong() {
         api.updateUserMobile("12345678901234567890", "18602506990");
    }

    @Test
    public void updateUserMobileBadUserId() {
        api.updateUserMobile("123456789012345678X", "18602506990");
    }

}
