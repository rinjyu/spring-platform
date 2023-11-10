package com.spring.batch.common.adapter;

import org.springframework.batch.item.adapter.ItemWriterAdapter;

import java.util.List;

public class BatchItemWriterAdapter<T> extends ItemWriterAdapter<T> {

    public BatchItemWriterAdapter() {
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        super.write(items);
    }
}
