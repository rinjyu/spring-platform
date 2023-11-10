package com.spring.batch.job.stepjob.reader;

import com.spring.batch.common.reader.BatchItemReader;
import com.spring.batch.job.common.domain.Inventory;

import java.util.ArrayList;
import java.util.List;


public class InventoryItemReader extends BatchItemReader<Inventory> {

    private List<Inventory> list;

    public InventoryItemReader(List<Inventory> list) {
        this.list = new ArrayList<>(list);
    }

    @Override
    public Inventory read() {
        return list.isEmpty() ? null : list.remove(0);
    }
}