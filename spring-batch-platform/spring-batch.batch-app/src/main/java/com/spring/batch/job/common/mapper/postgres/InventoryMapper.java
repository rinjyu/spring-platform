package com.spring.batch.job.common.mapper.postgres;

import com.spring.batch.common.config.datasource.marker.PostgresMapper;
import com.spring.batch.job.common.domain.Inventory;

import java.util.List;

@PostgresMapper
public interface InventoryMapper {

    Inventory getInventory(Inventory inventory);

    List<Inventory> getInventories(Inventory inventory);

    int upsertInventory(Inventory inventory);

    void callSpInventoryUsableQtyUpd(Inventory inventory);
}
