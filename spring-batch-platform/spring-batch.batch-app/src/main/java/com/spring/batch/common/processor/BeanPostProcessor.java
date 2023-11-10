package com.spring.batch.common.processor;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;

public class BeanPostProcessor extends JobRegistryBeanPostProcessor {

    public BeanPostProcessor() {

    }

    public BeanPostProcessor(JobRegistry jobRegistry) {
        setJobRegistry(jobRegistry);
    }
}
