package com.spring.batch.job.flowjob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FlowJobConfiguration2 {

    private static final String JOB_NAME = "flowJob2";
    private static final String STEP_NAME = "flowJob2_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job flowJob2() {
        return this.jobBuilderFactory.get(JOB_NAME)
                .start(flowJob2_step1())
                .on(BatchStatus.COMPLETED.name()).to(flowJob2_step2())
                .from(flowJob2_step1())
                .on(BatchStatus.FAILED.name()).to(flowJob2_step3())
                .end()
                .build();
    }

    @Bean(name = STEP_NAME + "1")
    public Step flowJob2_step1() {
        return stepBuilderFactory.get(STEP_NAME + "1")
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> flowJob2_Step1 <<<");
                    throw new RuntimeException("flowJob2_step1 is failed.");
                })
                .build();
    }

    @Bean(name = STEP_NAME + "2")
    public Step flowJob2_step2() {
        return stepBuilderFactory.get(STEP_NAME + "2")
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> flowJob2_step2 <<<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = STEP_NAME + "3")
    public Step flowJob2_step3() {
        return stepBuilderFactory.get(STEP_NAME + "3")
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> flowJob2_step3 <<<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
