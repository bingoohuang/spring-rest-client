package com.github.bingoohuang.springrestclient.spring;

import com.github.bingoohuang.springrestclient.generators.SpringRestClientFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringRestClientFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {
    private Class<T> interfaceClazz;
    private ApplicationContext appContext;

    public void setInterfaceClazz(Class<T> interfaceClazz) {
        this.interfaceClazz = interfaceClazz;
    }

    @Override
    public T getObject() {
        return SpringRestClientFactory.getRestClient(interfaceClazz, appContext);
    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceClazz;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.appContext = applicationContext;
    }
}
