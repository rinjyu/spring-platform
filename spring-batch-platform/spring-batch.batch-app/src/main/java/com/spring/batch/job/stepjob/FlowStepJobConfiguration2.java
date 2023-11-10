package com.spring.batch.job.stepjob;

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
public class FlowStepJobConfiguration2 {

    private static final String JOB_NAME = "flowStepJob2";
    private static final String FLOW_NAME = "flowStepJob2_flow";
    private static final String FLOW_STEP_NAME = "flowStepJob2_flowStep";
    private static final String STEP_NAME = "flowStepJob2_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job flowStepJob2() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(flowStepJob2_flowStep())
                .next(flowStepJob2_step2())
                .build();
    }

    @Bean(name = FLOW_STEP_NAME)
    public Step flowStepJob2_flowStep() {
        return stepBuilderFactory.get(FLOW_STEP_NAME)
                .flow(flowStepJob2_flow())
                .build();
    }

    @Bean(name = FLOW_NAME)
    public Flow flowStepJob2_flow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>(FLOW_NAME);
        flowBuilder.start(flowStepJob2_step1())
                .end();
        return flowBuilder.build();
    }

    @Bean(name = STEP_NAME + "1")
    public Step flowStepJob2_step1() {
        return stepBuilderFactory.get(STEP_NAME + "1")
                .tasklet((contribution, chunkContext) -> {
                    throw new RuntimeException("flowStepJob2_step1 was failed");
                })
                .build();
    }

    @Bean(name = STEP_NAME + "2")
    public Step flowStepJob2_step2() {
        return stepBuilderFactory.get(STEP_NAME + "2")
                .tasklet((contribution, chunkContext) -> {
                    log.debug(">> flowStepJob2_step2 <<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
