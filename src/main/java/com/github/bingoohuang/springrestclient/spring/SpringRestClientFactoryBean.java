package com.github.bingoohuang.springrestclient.spring;

import com.github.bingoohuang.springrestclient.generators.SpringRestClientFactory;
import org.springframework.beans.factory.FactoryBean;

public class SpringRestClientFactoryBean<T> implements FactoryBean<T> {
    private Class<T> interfaceClazz;

    public void setInterfaceClazz(Class<T> interfaceClazz) {
        this.interfaceClazz = interfaceClazz;
    }

    @Override
    public T getObject() throws Exception {
        return SpringRestClientFactory.getRestClient(interfaceClazz);
    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceClazz;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
