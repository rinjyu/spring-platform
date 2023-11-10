package com.spring.batch.common.adapter;

import org.springframework.batch.item.adapter.ItemProcessorAdapter;

public class BatchItemProcessorAdapter<I, O> extends ItemProcessorAdapter<I, O> {

    public BatchItemProcessorAdapter() {
    }

    @Override
    public O process(I item) throws Exception {
        return super.process(item);
    }
}
