package com.spring.batch.common.processor;


import org.springframework.batch.item.ItemProcessor;

public class BatchItemProcessor<I, O> implements ItemProcessor<I, O> {

    @Override
    public O process(I i) throws Exception {
        return null;
    }
}
