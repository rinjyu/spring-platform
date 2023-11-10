package com.spring.batch.job.simplejob;

import com.spring.batch.job.simplejob.listener.SimpleJobExecutionListener;
import com.spring.batch.job.simplejob.listener.SimpleJobStepExecutionListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleJobConfiguration5 {

    private static final String JOB_NAME = "simpleJob5";
    private static final String STEP_NAME = "simpleJob5_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job simpleJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(simpleJob_step())
                .incrementer(new RunIdIncrementer())
                .listener(new SimpleJobExecutionListener())
                .build();
    }

    @JobScope
    @Bean(name = STEP_NAME)
    public Step simpleJob_step() {
        return stepBuilderFactory.get(STEP_NAME)
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> simpleJob5_step START <<<");
                    log.debug(">>> simpleJob5_step END <<<");
                    return RepeatStatus.FINISHED;
                })
                .listener(new SimpleJobStepExecutionListener())
                .build();
    }
}
