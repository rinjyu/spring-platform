package com.spring.batch.job.stepjob.tasklet;

import com.spring.batch.common.tasklet.BatchTasklet;
import com.spring.batch.job.common.domain.Inventory;
import com.spring.batch.job.common.domain.Item;
import com.spring.batch.job.common.mapper.oracle.ItemMapper;
import com.spring.batch.job.common.mapper.postgres.InventoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class TaskletStepJobBatchTasklet extends BatchTasklet {

    @Value("#{jobParameters['itemId']}")
    private String itemId;

    @Value("#{jobParameters['unitItemId']}")
    private String unitItemId;

    private final ItemMapper itemMapper;
    private final InventoryMapper inventoryMapper;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        super.execute(stepContribution, chunkContext);

        Item itemParameter = Item.builder()
                                    .itemId(itemId)
                                    .unitItemId(unitItemId)
                                    .build();
        log.debug("itemParameter = {}", itemParameter);

        Item item = itemMapper.getItem(itemParameter);
        log.debug("item = {}", item);

        Inventory inventoryParameter = Inventory.builder()
                                                .itemId(itemId)
                                                .unitItemId(unitItemId)
                                                .build();
        log.debug("inventoryParameter = {}", inventoryParameter);

        Inventory inventory = inventoryMapper.getInventory(inventoryParameter);
        log.debug("inventory = {}", inventory);
        log.debug("=======================");

        return RepeatStatus.FINISHED;
    }
}
