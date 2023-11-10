package com.spring.batch.job.removejob;

import com.spring.batch.common.incrementer.BatchJobParametersIncrementer;
import com.spring.batch.job.removejob.tasklet.RemoveHistoryJobBatchTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RemoveHistoryJobConfiguration {

    private static final String JOB_NAME = "removeHistoryJob";
    private static final String STEP_NAME = "removeHistoryJob_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final RemoveHistoryJobBatchTasklet removeHistoryJobBatchTasklet;

    @Bean(name = JOB_NAME)
    public Job removeHistoryJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(removeHistoryJob_step())
                .incrementer(new BatchJobParametersIncrementer())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step removeHistoryJob_step() {
        return stepBuilderFactory.get(STEP_NAME)
                .tasklet(removeHistoryJobBatchTasklet)
                .build();
    }
}
