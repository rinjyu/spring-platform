package com.spring.batch.job.kafkajob;

import com.spring.batch.job.common.domain.Inventory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.kafka.KafkaItemWriter;
import org.springframework.batch.item.kafka.builder.KafkaItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaItemWriterJobConfiguration {

    private static final String JOB_NAME = "kafkaItemWriterJob";
    private static final String STEP_NAME = "kafkaItemWriterJob_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("kafkaItemWriterInventoryTemplate")
    private KafkaTemplate<String, Inventory> kafkaItemWriterInventoryTemplate;

    private final Jaxb2Marshaller jaxb2Marshaller;

    @Bean(name = JOB_NAME)
    public Job kafkaItemWriterJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(kafkaItemWriterJob_step())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step kafkaItemWriterJob_step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Inventory, Inventory>chunk(5)
                .reader(inventoryStaxEventKafkaItemReader())
                .processor((ItemProcessor<Inventory, Inventory>) item -> {
                    log.debug("{}", item);
                    return item;
                })
                .writer(inventoryKafkaItemWriter())
                .build();
    }

    @Bean
    public StaxEventItemReader<Inventory> inventoryStaxEventKafkaItemReader() {
        return new StaxEventItemReaderBuilder<Inventory>()
                .name("inventoryStaxEventKafkaItemReader")
                .resource(new ClassPathResource("data/inventory.xml"))
                .addFragmentRootElements("inventory")
                .unmarshaller(jaxb2Marshaller)
                .build();
    }

    @Bean
    public KafkaItemWriter<String, Inventory> inventoryKafkaItemWriter() {
        return new KafkaItemWriterBuilder<String, Inventory>()
                .kafkaTemplate(kafkaItemWriterInventoryTemplate)
                .itemKeyMapper(inventory -> String.format("%s:%s", inventory.getItemId(), inventory.getUnitItemId()))
                .delete(false)
                .build();
    }
}
