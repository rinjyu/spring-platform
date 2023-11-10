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
public class TransitionJobConfiguration {

    private static final String JOB_NAME = "transitionJob";
    private static final String STEP_NAME = "transitionJob_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job transitionJob() {
        return this.jobBuilderFactory.get(JOB_NAME)
                .start(transitionJob_step1())
                    .on(ExitStatus.FAILED.getExitCode())
                    .to(transitionJob_step2())
                    .on("*")
                    .stop()
                .from(transitionJob_step1())
                    .on("*")
                    .to(transitionJob_step3())
                    .next(transitionJob_step4())
                    .on(ExitStatus.FAILED.getExitCode())
                    .end()
                .end()
                .build();
    }

    @Bean(name = STEP_NAME + "1")
    public Step transitionJob_step1() {
        return stepBuilderFactory.get(STEP_NAME + "1")
                .tasklet((contribution, chunkContext) -> {
                    log.debug(">> transitionJob_step1 <<");
                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = STEP_NAME + "2")
    public Step transitionJob_step2() {
        return stepBuilderFactory.get("transitionJob_step2")
                .tasklet((contribution, chunkContext) -> {
                    log.debug(">> transitionJob_step2 <<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = STEP_NAME + "3")
    public Step transitionJob_step3() {
        return stepBuilderFactory.get(STEP_NAME + "3")
                .tasklet((contribution, chunkContext) -> {
                    log.debug(">> transitionJob_step3 <<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = STEP_NAME + "4")
    public Step transitionJob_step4() {
        return stepBuilderFactory.get(STEP_NAME + "4")
                .tasklet((contribution, chunkContext) -> {
                    log.debug(">> transitionJob_step4 <<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}