package com.spring.batch.job.simplejob;

import com.spring.batch.job.simplejob.tasklet.SimpleJobParameterBatchTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleJobConfiguration4 {

    private static final String JOB_NAME = "simpleJob4";
    private static final String STEP_NAME = "simpleJob4_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SimpleJobParameterBatchTasklet simpleJobParameterTasklet;

    @Bean(name = JOB_NAME)
    public Job simpleJob4() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(simpleJob4_step1(null))
                .next(simpleJob4_step2(null))
                .build();
    }

    @JobScope
    @Bean(name = STEP_NAME + "1")
    public Step simpleJob4_step1(@Value("#{jobParameters['message']}") String message) {
        return stepBuilderFactory.get(STEP_NAME)
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> simpleJob4_step1 <<<");
                    simpleJobParameterTasklet.message(message);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @JobScope
    @Bean(name = STEP_NAME + "2")
    public Step simpleJob4_step2(@Value("#{jobParameters['message']}") String message) {
        return stepBuilderFactory.get(STEP_NAME + "2")
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> simpleJob4_step2 <<<");
                    log.debug("message = {}", message);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
