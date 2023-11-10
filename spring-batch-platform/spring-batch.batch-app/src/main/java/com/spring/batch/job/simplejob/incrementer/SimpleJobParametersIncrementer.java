package com.spring.batch.job.simplejob.incrementer;

import com.spring.batch.common.incrementer.BatchJobParametersIncrementer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.util.ObjectUtils;

@Slf4j
public class SimpleJobParametersIncrementer extends BatchJobParametersIncrementer {

    private static String RUN_ID_KEY = "run.id";

    @Override
    public JobParameters getNext(JobParameters jobParameters) {
        JobParameters parameters = ObjectUtils.isEmpty(super.getNext(jobParameters)) ? new JobParameters() : super.getNext(jobParameters);
        Long id = parameters.getLong(RUN_ID_KEY, Long.valueOf(0)) + 1;
        return new JobParametersBuilder(parameters).addLong(RUN_ID_KEY, id).toJobParameters();
    }
}
