package com.github.bingoohuang.springrestclient.boot.advisor;

import com.github.bingoohuang.springrestclient.boot.interceptor.ThreadLocalInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
public class NullReturnValueInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            Object retValue = invocation.proceed();

            nullProcess(retValue);

            return retValue;
        } catch (Throwable throwable) {
            throw throwable;
        }
    }

    private void nullProcess(Object retValue) {
        if (retValue != null) return;

        HttpServletResponse response = ThreadLocalInterceptor.getResponse();
        response.addHeader("returnNull", "true");
    }
}
