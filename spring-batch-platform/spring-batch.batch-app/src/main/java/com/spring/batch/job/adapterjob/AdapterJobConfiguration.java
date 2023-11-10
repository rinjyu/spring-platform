package com.spring.batch.job.adapterjob;

import com.spring.batch.common.adapter.BatchItemWriterAdapter;
import com.spring.batch.common.converter.BatchItemToParameterConverter;
import com.spring.batch.common.reader.OracleMybatisCursorItemReader;
import com.spring.batch.job.common.domain.Inventory;
import com.spring.batch.job.common.domain.Item;
import com.spring.batch.job.common.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AdapterJobConfiguration {

    private static final String JOB_NAME = "adapterJob";
    private static final String STEP_NAME = "adapterJob_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final InventoryService inventoryService;

    @Bean(name = JOB_NAME)
    public Job adapterJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(adapterJob_step())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step adapterJob_step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Item, Inventory>chunk(2)
                .reader(oracleItemReader(null, null))
                .processor((ItemProcessor<Item, Inventory>) item -> {
                    Inventory inventory = Inventory.builder()
                            .itemId(item.getItemId())
                            .unitItemId(item.getUnitItemId())
                            .build();
                    log.debug("{}", inventory);
                    return inventory;
                })
                .writer(inventoryItemWriterAdapter())
                .build();
    }

    @Bean
    @StepScope
    public MyBatisCursorItemReader<Item> oracleItemReader(@Value("#{jobParameters['itemId']}") String itemId,
                                                            @Value("#{jobParameters['unitItemId']}") String unitItemId) {
        Map<String, Object> parameters = BatchItemToParameterConverter.mapConvert(Item.builder()
                                                                                    .itemId(itemId)
                                                                                    .unitItemId(unitItemId)
                                                                                    .build());
        return new OracleMybatisCursorItemReader<Item>()
                .queryId("com.spring.batch.job.common.mapper.oracle.ItemMapper.getItem")
                .parameterValues(parameters)
                .build();
    }

    @Bean
    public BatchItemWriterAdapter<Inventory> inventoryItemWriterAdapter() {
        BatchItemWriterAdapter<Inventory> batchItemWriterAdapter = new BatchItemWriterAdapter<>();
        batchItemWriterAdapter.setTargetObject(inventoryService);
        batchItemWriterAdapter.setTargetMethod("upsertInventory");
        return batchItemWriterAdapter;
    }
}
