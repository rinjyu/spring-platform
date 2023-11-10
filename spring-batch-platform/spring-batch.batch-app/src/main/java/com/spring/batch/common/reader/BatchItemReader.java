package com.spring.batch.common.reader;


import org.springframework.batch.item.ItemReader;

public class BatchItemReader<T> implements ItemReader<T> {

    @Override
    public T read() throws Exception {
        return null;
    }
}