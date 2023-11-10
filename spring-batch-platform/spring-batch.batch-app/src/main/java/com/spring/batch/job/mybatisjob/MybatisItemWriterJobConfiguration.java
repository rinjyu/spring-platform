package com.spring.batch.job.mybatisjob;

import com.spring.batch.common.converter.BatchItemToParameterConverter;
import com.spring.batch.common.reader.OracleMybatisCursorItemReader;
import com.spring.batch.common.writer.PostgresMybatisItemWriter;
import com.spring.batch.job.common.domain.Inventory;
import com.spring.batch.job.common.domain.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
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
public class MybatisItemWriterJobConfiguration {

    private static final String JOB_NAME = "mybatisItemWriterJob";
    private static final String STEP_NAME = "mybatisItemWriterJob_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job mybatisItemWriterJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(mybatisItemWriter_step())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step mybatisItemWriter_step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Item, Inventory>chunk(1)
                .reader(oracleMybatisCursorSampleReader(null, null))
                .processor((ItemProcessor<Item, Inventory>) item -> {
                    Inventory inventory = Inventory.builder()
                            .itemId(item.getItemId())
                            .unitItemId(item.getUnitItemId())
                            .build();
                    log.debug("{}", inventory);
                    return inventory;
                })
                .writer(postgresMybatisItemBatchSampleWriter())
                .build();
    }

    @Bean
    @StepScope
    public MyBatisCursorItemReader<Item> oracleMybatisCursorSampleReader(@Value("#{jobParameters['itemId']}") String itemId,
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
    @StepScope
    public MyBatisBatchItemWriter<Inventory> postgresMybatisItemBatchSampleWriter() {
        return new PostgresMybatisItemWriter<Inventory>()
                .statementId("com.spring.batch.job.common.mapper.postgres.InventoryMapper.upsertInventory")
                .assertUpdates(false)
                .build();
    }
}
