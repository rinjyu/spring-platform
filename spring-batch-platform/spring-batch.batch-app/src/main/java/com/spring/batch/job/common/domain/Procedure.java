package com.spring.batch.job.common.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@ToString
@NoArgsConstructor
public class Procedure {

    private String resultCode;
    private String resultMessage;
}
