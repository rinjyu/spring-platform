package com.spring.batch.common.config.kafka;

import com.spring.batch.job.common.domain.Inventory;
import com.spring.batch.job.common.enums.kafka.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, Inventory> inventoryProducerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
    }

    @Bean
    public KafkaTemplate<String, Inventory> kafkaItemWriterInventoryTemplate() {
        KafkaTemplate<String, Inventory> kafkaTemplate = new KafkaTemplate<>(inventoryProducerFactory());
        kafkaTemplate.setDefaultTopic(Topic.TOPIC_INVENTORY.getName());
        return kafkaTemplate;
    }
}
