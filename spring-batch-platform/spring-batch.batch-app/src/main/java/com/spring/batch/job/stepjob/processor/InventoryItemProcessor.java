package com.spring.batch.job.stepjob.processor;

import com.spring.batch.common.processor.BatchItemProcessor;
import com.spring.batch.job.common.domain.Inventory;


public class InventoryItemProcessor extends BatchItemProcessor<Inventory, Inventory> {

    @Override
    public Inventory process(Inventory inventory) {
        return Inventory.builder()
                .itemId(inventory.getItemId())
                .usableQty(inventory.getUsableQty() * 2)
                .build();
    }
}