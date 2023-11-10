package com.spring.batch.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProcedureStatus {

    SUCCESS_00("00"),
    FAIL_99("99")
    ;

    private String statusCode;

    public String getKey() {
        return name();
    }
}
