package com.github.bingoohuang.springrestclient.spring;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Slf4j
public class ClassPathSpringRestClientScanner extends ClassPathBeanDefinitionScanner {
    public ClassPathSpringRestClientScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    /**
     * Configures parent scanner to search for the right interfaces. It can search
     * for all interfaces or just for those that extends a markerInterface or/and
     * those annotated with the annotationClass
     */
    public void registerFilters() {
        addExcludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                return !metadataReader.getClassMetadata().isInterface();
            }
        });
        addIncludeFilter(new AnnotationTypeFilter(SpringRestClientEnabled.class));
    }

    /**
     * Calls the parent search that will search and register all the candidates.
     * Then the registered objects are post processed to set them as
     * MapperFactoryBeans
     */
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        val beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            log.warn("No SpringRestClientEnabled was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            for (BeanDefinitionHolder holder : beanDefinitions) {
                val definition = (GenericBeanDefinition) holder.getBeanDefinition();

                if (log.isDebugEnabled()) {
                    log.debug("Creating SpringRestClientFactoryBean with name '" + holder.getBeanName()
                        + "' and '" + definition.getBeanClassName() + "' interfaceClazz");
                }

                // the mapper interface is the original class of the bean
                // but, the actual class of the bean is MapperFactoryBean
                definition.getPropertyValues().add("interfaceClazz", definition.getBeanClassName());
                definition.setBeanClass(SpringRestClientFactoryBean.class);
            }
        }

        return beanDefinitions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            log.warn("Skipping SpringRestClientFactoryBean with name '" + beanName
                + "' and '" + beanDefinition.getBeanClassName() + "' interfaceClazz"
                + ". Bean already defined with the same name!");
            return false;
        }
    }

}
