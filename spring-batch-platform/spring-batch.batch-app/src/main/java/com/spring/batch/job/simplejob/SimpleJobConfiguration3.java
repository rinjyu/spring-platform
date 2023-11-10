package com.spring.batch.job.simplejob;

import com.spring.batch.job.simplejob.incrementer.SimpleJobParametersIncrementer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleJobConfiguration3 {

    private static final String JOB_NAME = "simpleJob3";
    private static final String STEP_NAME = "simpleJob3_step";
    private static final String TASKLET_NAME = "simpleJob3_tasklet";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job simpleJob3() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(simpleJob3_step1(null))
                .next(simpleJob3_step2())
                .incrementer(new SimpleJobParametersIncrementer())
                .build();
    }

    @JobScope
    @Bean(name = STEP_NAME + "1")
    public Step simpleJob3_step1(@Value("#{jobParameters['requestDate']}") String requestDate) {
        log.debug(">> simpleJob3_step1 ::: requestDate = {}", requestDate);
        return stepBuilderFactory.get(STEP_NAME + "1")
                .tasklet(simpleJob3_tasklet1(null))
                .allowStartIfComplete(true)
                .build();
    }

    @Bean(name = STEP_NAME + "2")
    public Step simpleJob3_step2() {
        return stepBuilderFactory.get(STEP_NAME + "2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        throw new RuntimeException("simpleJob3_step2 was failed");
                    }
                })
                .startLimit(3)
                .build();
    }

    @StepScope
    @Bean(name = TASKLET_NAME + "1")
    public Tasklet simpleJob3_tasklet1(@Value("#{jobParameters['runner']}") String runner){
        return (stepContribution, chunkContext) -> {
            log.debug(">> simpleJob3_tasklet1 ::: runner = {}", runner);
            return RepeatStatus.FINISHED;
        };
    }
}
