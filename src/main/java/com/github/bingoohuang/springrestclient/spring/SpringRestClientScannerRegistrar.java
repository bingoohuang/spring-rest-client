package com.github.bingoohuang.springrestclient.spring;


import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;


public class SpringRestClientScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    /**
     * {@inheritDoc}
     */
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        val name = SpringRestClientEnabledScan.class.getName();
        val annotationAttributes = importingClassMetadata.getAnnotationAttributes(name);
        val scanner = new ClassPathSpringRestClientScanner(registry);

        if (resourceLoader != null) { // this check is needed in Spring 3.1
            scanner.setResourceLoader(resourceLoader);
        }

        val annoAttrs = AnnotationAttributes.fromMap(annotationAttributes);
        Class<? extends BeanNameGenerator> generatorClass;
        generatorClass = annoAttrs.getClass("nameGenerator");
        if (!BeanNameGenerator.class.equals(generatorClass)) {
            scanner.setBeanNameGenerator(BeanUtils.instantiateClass(generatorClass));
        }

        val basePackages = new ArrayList<String>();
        for (val pkg : annoAttrs.getStringArray("value")) {
            if (StringUtils.hasText(pkg)) basePackages.add(pkg);
        }

        for (val pkg : annoAttrs.getStringArray("basePackages")) {
            if (StringUtils.hasText(pkg)) basePackages.add(pkg);
        }

        for (val clazz : annoAttrs.getClassArray("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        if (basePackages.isEmpty()) {
            String className = importingClassMetadata.getClassName();
            basePackages.add(ClassUtils.getPackageName(className));
        }

        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }

    /**
     * {@inheritDoc}
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}
