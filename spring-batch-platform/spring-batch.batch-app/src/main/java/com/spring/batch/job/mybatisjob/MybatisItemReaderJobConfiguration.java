package com.spring.batch.job.mybatisjob;

import com.spring.batch.common.converter.BatchItemToParameterConverter;
import com.spring.batch.common.reader.OracleMybatisCursorItemReader;
import com.spring.batch.common.reader.OracleMybatisPagingItemReader;
import com.spring.batch.job.common.domain.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MybatisItemReaderJobConfiguration {

    private static final String JOB_NAME = "mybatisItemReaderJob";
    private static final String STEP_NAME = "mybatisItemReaderJob_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job mybatisItemReaderJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(mybatisItemReaderJob_step1())
                .next(mybatisItemReaderJob_step2())
                .build();
    }

    @Bean(name = STEP_NAME + "1")
    public Step mybatisItemReaderJob_step1() {
        return stepBuilderFactory.get(STEP_NAME + "1")
                .<Item, Item>chunk(1)
                .reader(oracleMybatisCursorItemReader(null, null))
                .writer(items -> {
                    for (Item item : items) {
                        log.debug("==========");
                        log.debug("{}", item);
                        log.debug("==========");
                    }
                })
                .build();
    }

    @Bean(name = STEP_NAME + "2")
    public Step mybatisItemReaderJob_step2() {
        return stepBuilderFactory.get(STEP_NAME + "2")
                .<Item, Item>chunk(1)
                .reader(oracleMybatisPagingItemReader(null, null))
                .writer(items -> {
                    for (Item item : items) {
                        log.debug("==========");
                        log.debug("{}", item);
                        log.debug("==========");
                    }
                })
                .build();
    }

    @Bean
    @StepScope
    public MyBatisCursorItemReader<Item> oracleMybatisCursorItemReader(@Value("#{jobParameters['itemId']}") String itemId,
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
    public MyBatisPagingItemReader<Item> oracleMybatisPagingItemReader(@Value("#{jobParameters['itemId']}") String itemId,
                                                                        @Value("#{jobParameters['unitItemId']}") String unitItemId) {
        Map<String, Object> parameters = BatchItemToParameterConverter.mapConvert(Item.builder()
                .itemId(itemId)
                .unitItemId(unitItemId)
                .build());

        return new OracleMybatisPagingItemReader<Item>()
                .queryId("com.spring.batch.job.common.mapper.oracle.ItemMapper.getItem")
                .parameterValues(parameters)
                .build();
    }
}
