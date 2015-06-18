package com.github.bingoohuang.springrestclient.boot.resolver;


import com.google.common.collect.Lists;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.LinkedList;

@Component
public class HiPostProcessor implements BeanFactoryAware, BeanPostProcessor, InitializingBean {
    private ConfigurableBeanFactory beanFactory;

    public Object postProcessAfterInitialization(Object bean, String name) {
        if (bean instanceof RequestMappingHandlerAdapter) {
            LinkedList<HandlerMethodArgumentResolver> resolvers = Lists.newLinkedList();
            resolvers.add(new HiRequestParamMethodArgumentResolver(beanFactory, false));
            resolvers.addAll(((RequestMappingHandlerAdapter) bean).getArgumentResolvers());

            new BeanWrapperImpl(bean).setPropertyValue("argumentResolvers",
                    new HandlerMethodArgumentResolverComposite().addResolvers(resolvers));
        }

        return bean;
    }

    public Object postProcessBeforeInitialization(Object bean, String name) {
        return bean;
    }

    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof ConfigurableBeanFactory) {
            this.beanFactory = (ConfigurableBeanFactory) beanFactory;
        }
    }
}