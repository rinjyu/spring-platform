package com.spring.batch.job.stepjob;

import com.spring.batch.job.common.domain.Inventory;
import com.spring.batch.job.stepjob.processor.InventoryItemProcessor;
import com.spring.batch.job.stepjob.reader.InventoryItemReader;
import com.spring.batch.job.stepjob.writer.InventoryItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@Configuration
@RequiredArgsConstructor
public class ChunkStepJobConfiguration {

    private static final String JOB_NAME = "chunkStepJob";
    private static final String STEP_NAME = "chunkStepJob_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job chunkStepJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(chunkStepJob_step())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step chunkStepJob_step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Inventory, Inventory>chunk(3)
                .reader(chunkStepJob_ItemReader())
                .processor(chunkStepJob_ItemProcessor())
                .writer(chunkStepJob_ItemWriter())
                .build();
    }

    @Bean
    public ItemReader<Inventory> chunkStepJob_ItemReader() {
        Inventory inventory1 = Inventory.builder()
                .itemId("0000000000004")
                .unitItemId("00000")
                .usableQty(100)
                .build();

        Inventory inventory2 = Inventory.builder()
                .itemId("0000000000005")
                .unitItemId("00000")
                .usableQty(100)
                .build();

        Inventory inventory3 = Inventory.builder()
                .itemId("0000000000006")
                .unitItemId("00000")
                .usableQty(100)
                .build();

        return new InventoryItemReader(Arrays.asList(inventory1, inventory2, inventory3));
    }

    @Bean
    public ItemProcessor<Inventory, Inventory> chunkStepJob_ItemProcessor() {
        return new InventoryItemProcessor();
    }

    @Bean
    public ItemWriter<? super Inventory> chunkStepJob_ItemWriter() {
        return new InventoryItemWriter();
    }
}
