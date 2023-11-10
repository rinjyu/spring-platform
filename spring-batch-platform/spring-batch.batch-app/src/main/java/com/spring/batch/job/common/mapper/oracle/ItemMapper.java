package com.spring.batch.job.common.mapper.oracle;

import com.spring.batch.common.config.datasource.marker.OracleMapper;
import com.spring.batch.job.common.domain.Item;

import java.util.List;

@OracleMapper
public interface ItemMapper {

    Item getItem(Item item);

    List<Item> getItems(Item item);
}
