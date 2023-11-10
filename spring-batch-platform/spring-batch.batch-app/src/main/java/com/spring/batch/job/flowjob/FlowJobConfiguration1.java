package com.spring.batch.job.flowjob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
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
public class FlowJobConfiguration1 {

    private static final String JOB_NAME = "flowJob1";
    private static final String STEP_NAME = "flowJob1_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job flowJob1() {
        return this.jobBuilderFactory.get(JOB_NAME)
                .start(flowJob1_step1())
                .on(ExitStatus.COMPLETED.getExitCode()).to(flowJob1_step2())
                .from(flowJob1_step1())
                .on(ExitStatus.FAILED.getExitCode()).to(flowJob1_step3())
                .end()
                .build();
    }

    @Bean(name = STEP_NAME + "1")
    public Step flowJob1_step1() {
        return stepBuilderFactory.get(STEP_NAME + "1")
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> flowJob1_step1 <<<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = STEP_NAME + "2")
    public Step flowJob1_step2() {
        return stepBuilderFactory.get(STEP_NAME + "2")
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> flowJob1_step2 <<<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = STEP_NAME + "3")
    public Step flowJob1_step3() {
        return stepBuilderFactory.get(STEP_NAME + "3")
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> flowJob1_step3 <<<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
