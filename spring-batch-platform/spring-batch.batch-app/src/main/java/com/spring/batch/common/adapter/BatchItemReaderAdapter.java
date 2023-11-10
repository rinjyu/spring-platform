package com.spring.batch.common.adapter;

import org.springframework.batch.item.adapter.ItemReaderAdapter;

import java.util.Objects;

public class BatchItemReaderAdapter<T> extends ItemReaderAdapter<T> {

    public BatchItemReaderAdapter() {
    }

    @Override
    public T read() throws Exception {
        T item = super.read();
        return Objects.isNull(item) ? null : item;
    }
}
