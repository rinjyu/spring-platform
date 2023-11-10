package com.spring.batch.job.stepjob.writer;

import com.spring.batch.common.writer.BatchItemWriter;
import com.spring.batch.job.common.domain.Inventory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class InventoryItemWriter extends BatchItemWriter<Inventory> {

    @Override
    public void write(List<? extends Inventory> list) {
        list.forEach(inventory -> log.debug("{}", inventory));
    }
}
