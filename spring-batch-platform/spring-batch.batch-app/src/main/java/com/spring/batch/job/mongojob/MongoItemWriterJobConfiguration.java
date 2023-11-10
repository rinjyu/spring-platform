package com.spring.batch.job.mongojob;

import com.spring.batch.job.common.domain.Inventory;
import com.spring.batch.job.common.enums.mongo.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MongoItemWriterJobConfiguration {

    private static final String JOB_NAME = "mongoItemWriterJob";
    private static final String STEP_NAME = "mongoItemWriterJob_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MongoTemplate mongoTemplate;

    @Bean(name = JOB_NAME)
    public Job mongoItemWriterJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(mongoItemWriterJob_step())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step mongoItemWriterJob_step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Inventory, Inventory>chunk(5)
                .reader(inventoryStaxEventItemReader())
                .writer(inventoryMongoItemWriter())
                .build();
    }

    @Bean
    public StaxEventItemReader<Inventory> inventoryStaxEventItemReader() {
        return new StaxEventItemReaderBuilder<Inventory>()
                .name("inventoryStaxEventItemReader")
                .resource(new ClassPathResource("data/inventory.xml"))
                .addFragmentRootElements("inventory")
                .unmarshaller(inventoryXStreamMarshaller())
                .build();
    }

    @Bean
    public MongoItemWriter<Inventory> inventoryMongoItemWriter() {
        return new MongoItemWriterBuilder<Inventory>()
                .template(mongoTemplate)
                .collection(Collection.INVENTORY.getName())
                .build();
    }

    @Bean
    public XStreamMarshaller inventoryXStreamMarshaller() {
        Map<String, Class<?>> aliases = new HashMap<>();
        aliases.put("inventory", Inventory.class);
        aliases.put("itemId", String.class);
        aliases.put("unitItemId", String.class);
        aliases.put("usableQty", Integer.class);

        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
        xStreamMarshaller.setAliases(aliases);
        return xStreamMarshaller;
    }
}
