package com.spring.batch.job.common.service;

import com.spring.batch.job.common.domain.Inventory;
import com.spring.batch.job.common.mapper.postgres.InventoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryMapper inventoryMapper;

    public Inventory getInventory(Inventory inventory) {
        return inventoryMapper.getInventory(inventory);
    }

    public List<Inventory> getInventories(Inventory inventory) {
        return inventoryMapper.getInventories(inventory);
    }

    public int upsertInventory(Inventory inventory) {
        return inventoryMapper.upsertInventory(inventory);
    }

    public void callSpInventoryUsableQtyUpd(Inventory inventory) {
        inventoryMapper.callSpInventoryUsableQtyUpd(inventory);
    }
}