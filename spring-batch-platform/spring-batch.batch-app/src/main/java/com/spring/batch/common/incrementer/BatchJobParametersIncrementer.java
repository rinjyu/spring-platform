package com.spring.batch.common.incrementer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Slf4j
public class BatchJobParametersIncrementer implements JobParametersIncrementer {

    private static String RUN_DATE_KEY = "run.date";

    @Override
    public JobParameters getNext(JobParameters jobParameters) {
        return (new JobParametersBuilder(ObjectUtils.isEmpty(jobParameters) ? new JobParameters() : jobParameters))
                .addDate(RUN_DATE_KEY, new Date())
                .toJobParameters();
    }
}