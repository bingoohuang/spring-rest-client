package com.github.bingoohuang.springrestclient.tests.api;

import com.github.bingoohuang.springrestclient.exception.RestException;
import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.ExApi;
import com.github.bingoohuang.springrestclient.spring.exception.BadArgumentError;
import com.github.bingoohuang.springrestclient.spring.exception.NotFoundError;
import com.github.bingoohuang.springrestclient.spring.exception.OtherError;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class ExApiTest {
    @Autowired
    ExApi exApi;

    @Test
    public void exceptionNotFound() {
        try {
            exApi.exception(1);
            fail();
        } catch (RestException e) {
            assertThat(e.getStatus(), is(equalTo(404)));
        }
    }

    @Test
    public void notFound() throws BadArgumentError {
        try {
            exApi.error(1);
            fail();
        } catch (NotFoundError ex) {
        }
    }

    @Test
    public void exceptionBadArg() {
        try {
            exApi.exception(2);
            fail();
        } catch (RestException e) {
            assertThat(e.getStatus(), is(equalTo(405)));
        }
    }

    @Test
    public void badArgumentError() throws NotFoundError {
        try {
            exApi.error(2);
            fail();
        } catch (BadArgumentError ex) {
            assertThat(ex.getMessage(), is(equalTo("BadArgumentException ErrorMsg")));
        }
    }

    @Test
    public void exceptionRuntime() {
        try {
            exApi.exception(3);
            fail();
        } catch (RestException e) {
            assertThat(e.getStatus(), is(equalTo(500)));
        }
    }


    @Test
    public void runtimeException() throws NotFoundError, BadArgumentError {
        try {
            exApi.error(3);
            fail();
        } catch (RestException ex) {
            assertThat(ex.getMessage(), is(equalTo("RuntimeException ErrorMsg")));
        }
    }

    @Test
    public void exceptionRest() {
        try {
            exApi.exception(4);
            fail();
        } catch (RestException e) {
            assertThat(e.getStatus(), is(equalTo(406)));
        }
    }

    @Test
    public void restException() throws NotFoundError, BadArgumentError {
        try {
            exApi.error(4);
            fail();
        } catch (OtherError ex) {
            assertThat(ex.getMessage(), is(equalTo("RestException ErrorMsg")));
        }
    }

    @Test
    public void exceptionOk() {
        int error = exApi.exception(100);
        assertThat(error, is(equalTo(100)));
    }


    @Test
    public void ok() throws NotFoundError, BadArgumentError {
        int error = exApi.error(100);
        assertThat(error, is(equalTo(100)));
    }
}
