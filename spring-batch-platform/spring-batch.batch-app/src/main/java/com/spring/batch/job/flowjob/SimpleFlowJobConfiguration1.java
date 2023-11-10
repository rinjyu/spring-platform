package com.spring.batch.job.flowjob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleFlowJobConfiguration1 {

    private static final String JOB_NAME = "simpleFlowJob1";
    private static final String FLOW_NAME = "simpleFlowJob1_flow";
    private static final String STEP_NAME = "simpleFlowJob1_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job simpleFlowJob1() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(simpleFlowJob1_flow())
                .next(simpleFlowJob1_step3())
                .end()
                .build();
    }

    @Bean(name = FLOW_NAME)
    public Flow simpleFlowJob1_flow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>(FLOW_NAME);
        flowBuilder.start(simpleFlowJob1_step1())
                .next(simpleFlowJob1_step2())
                .end();
        return flowBuilder.build();
    }

    @Bean(name = STEP_NAME + "1")
    public Step simpleFlowJob1_step1() {
        return stepBuilderFactory.get(STEP_NAME + "1")
                .tasklet((contribution, chunkContext) -> {
                    log.debug(">> simpleFlowJob1_step1 <<");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean(name = STEP_NAME + "2")
    public Step simpleFlowJob1_step2() {
        return stepBuilderFactory.get(STEP_NAME + "2")
                .tasklet((contribution, chunkContext) -> {
                    log.debug(">> simpleFlowJob1_step2 <<");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean(name = STEP_NAME + "3")
    public Step simpleFlowJob1_step3() {
        return stepBuilderFactory.get(STEP_NAME + "3")
                .tasklet((contribution, chunkContext) -> {
                    log.debug(">> simpleFlowJob1_step3 <<");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}