package com.spring.batch.job.mongojob;

import com.spring.batch.job.common.domain.Inventory;
import com.spring.batch.job.common.enums.mongo.Collection;
import com.spring.batch.job.stepjob.writer.InventoryItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MongoItemReaderJobConfiguration {

    private static final String JOB_NAME = "mongoItemReaderJob";
    private static final String STEP_NAME = "mongoItemReaderJob_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MongoTemplate mongoTemplate;

    @Bean(name = JOB_NAME)
    public Job mongoItemReaderJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(mongoItemReaderJob_step())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step mongoItemReaderJob_step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Inventory, Inventory>chunk(5)
                .reader(inventoryMongoItemReader())
                .writer(inventoryItemWriter())
                .build();
    }

    @Bean
    public MongoItemReader<Inventory> inventoryMongoItemReader() {
        return new MongoItemReaderBuilder<Inventory>()
                .name("inventoryMongoItemReader")
                .template(mongoTemplate)
                .collection(Collection.INVENTORY.getName())
                .jsonQuery("{}")
                .sorts(Collections.singletonMap("_id", Sort.Direction.ASC))
                .targetType(Inventory.class)
                .build();
    }

    @Bean
    public ItemWriter<? super Inventory> inventoryItemWriter() {
        return new InventoryItemWriter();
    }
}
