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

            nullProcess(invocation, retValue);

            return retValue;
        } catch (Throwable throwable) {
            throw throwable;
        }
    }

    private void nullProcess(MethodInvocation invocation, Object retValue) {
        if (retValue != null) return;
        Class<?> returnType = invocation.getMethod().getReturnType();
        if (returnType == void.class) return;
        if (returnType == Void.class) return;


        HttpServletResponse response = ThreadLocalInterceptor.getResponse();
        response.addHeader("returnNull", "true");
    }
}
