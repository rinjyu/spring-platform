package com.spring.batch.common.writer;

import org.springframework.batch.item.ItemWriter;

import java.util.List;


public class BatchItemWriter<T> implements ItemWriter<T> {

    @Override
    public void write(List<? extends T> list) throws Exception {

    }
}