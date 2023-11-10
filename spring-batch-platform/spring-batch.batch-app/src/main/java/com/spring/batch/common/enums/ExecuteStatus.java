package com.spring.batch.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExecuteStatus {

    SUCCESS("SUCCESS"),
    FAIL("FAIL")
    ;

    private String status;

    public String getKey() {
        return name();
    }
}
