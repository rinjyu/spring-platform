package com.spring.batch.job.kafkajob;

import com.spring.batch.job.common.domain.Inventory;
import com.spring.batch.job.common.enums.kafka.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaItemReaderJobConfiguration {

    private static final String JOB_NAME = "kafkaItemReaderJob";
    private static final String STEP_NAME = "kafkaItemReaderJob_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final KafkaProperties kafkaProperties;

    @Bean(name = JOB_NAME)
    public Job kafkaItemReaderJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(kafkaItemReaderJob_step())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step kafkaItemReaderJob_step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Inventory, Inventory>chunk(5)
                .reader(inventoryKafkaItemReader())
                .processor((ItemProcessor<Inventory, Inventory>) item -> {
                    log.debug("item = {}", item);
                    return item;
                })
                .writer(items -> {
                    for (Inventory inventory : items) {
                        log.debug("==========");
                        log.debug("{}", inventory);
                        log.debug("==========");
                    }
                })
                .build();
    }

    @Bean
    public KafkaItemReader<String, Inventory> inventoryKafkaItemReader() {
        Properties properties = new Properties();
        properties.putAll(kafkaProperties.buildConsumerProperties());

        return new KafkaItemReaderBuilder<String, Inventory>()
                .name("inventoryKafkaItemReader")
                .partitions(0)
                .partitionOffsets(new HashMap<>())
                .consumerProperties(properties)
                .pollTimeout(Duration.ofSeconds(5))
                .topic(Topic.TOPIC_INVENTORY.getName())
                .build();
    }
}
