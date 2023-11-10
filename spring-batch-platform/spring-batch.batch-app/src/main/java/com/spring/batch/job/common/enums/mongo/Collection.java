package com.spring.batch.job.common.enums.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Collection {

    INVENTORY("inventory")
    ;

    private String name;
}
