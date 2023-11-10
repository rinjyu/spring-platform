package com.spring.batch.job.samplejob;

import com.spring.batch.common.incrementer.BatchJobParametersIncrementer;
import com.spring.batch.common.listener.BatchJobExecutionListener;
import com.spring.batch.common.listener.BatchStepExecutionListener;
import com.spring.batch.common.tasklet.BatchTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SampleJobConfiguration2 {

    private static final String JOB_NAME = "sampleJob2";
    private static final String STEP_NAME_PREFIX = "sampleJob2_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job sampleJob2() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(sampleJob2_step())
                .incrementer(new BatchJobParametersIncrementer())
                .listener(new BatchJobExecutionListener())
                .build();
    }

    @Bean(name = STEP_NAME_PREFIX)
    public Step sampleJob2_step() {
        return stepBuilderFactory.get(STEP_NAME_PREFIX)
                .tasklet(new BatchTasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        log.debug("sampleJob2 테스트");
                        return super.execute(stepContribution, chunkContext);
                    }
                })
                .listener(new BatchStepExecutionListener())
                .build();
    }
}