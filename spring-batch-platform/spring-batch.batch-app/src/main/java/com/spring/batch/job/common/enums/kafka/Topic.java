package com.spring.batch.job.common.enums.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Topic {

    TOPIC_INVENTORY("topic-inventory")
    ;

    private String name;
}
