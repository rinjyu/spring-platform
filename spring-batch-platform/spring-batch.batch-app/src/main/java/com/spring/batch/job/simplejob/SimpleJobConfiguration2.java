package com.spring.batch.job.simplejob;

import com.spring.batch.job.simplejob.incrementer.SimpleJobParametersIncrementer;
import com.spring.batch.job.simplejob.listener.SimpleJobExecutionListener;
import com.spring.batch.job.simplejob.validator.SimpleJobParametersValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleJobConfiguration2 {

    private static final String JOB_NAME = "simpleJob2";
    private static final String STEP_NAME = "simpleJob2_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job simpleJob2() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(simpleJob2_step1())
                .next(simpleJob2_step2())
                .next(simpleJob2_step3())
                .incrementer(new SimpleJobParametersIncrementer())
                .validator(new SimpleJobParametersValidator())
                .listener(new SimpleJobExecutionListener())
                .build();
    }

    @Bean(name = STEP_NAME + "1")
    public Step simpleJob2_step1() {
        return stepBuilderFactory.get(STEP_NAME + "1")
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> simpleJob2_step1 <<<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = STEP_NAME + "2")
    public Step simpleJob2_step2() {
        return stepBuilderFactory.get(STEP_NAME + "2")
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> simpleJob2_step2 <<<");
                    log.debug(">> JobParameters <<");
                    JobParameters jobParameters = stepContribution.getStepExecution().getJobParameters();
                    String requestDate = jobParameters.getString("requestDate");
                    String runner = jobParameters.getString("runner");
                    log.debug(">> requestDate = {}", requestDate);
                    log.debug(">> runner = {}", runner);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = STEP_NAME + "3")
    public Step simpleJob2_step3() {
        return stepBuilderFactory.get(STEP_NAME + "3")
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> simpleJob2_step3 <<<");
                    throw new RuntimeException("simpleJob2_step3 is failed.");
                })
                .build();
    }
}
