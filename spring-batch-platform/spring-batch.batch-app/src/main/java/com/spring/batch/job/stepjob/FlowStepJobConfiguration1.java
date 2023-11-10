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
public class FlowStepJobConfiguration1 {

    private static final String JOB_NAME = "flowStepJob1";
    private static final String FLOW_NAME = "flowStepJob1_flow";
    private static final String FLOW_STE_NAME = "flowStepJob1_flowStep";
    private static final String STEP_NAME = "flowStepJob1_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job flowStepJob1() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(flowStepJob1_flowStep())
                .next(flowStepJob1_step2())
                .build();
    }

    @Bean(name = FLOW_STE_NAME)
    public Step flowStepJob1_flowStep() {
        return stepBuilderFactory.get(FLOW_STE_NAME)
                .flow(flowStepJob1_flow())
                .build();
    }

    @Bean(name = FLOW_NAME)
    public Flow flowStepJob1_flow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>(FLOW_NAME);
        flowBuilder.start(flowStepJob1_step1())
                .end();
        return flowBuilder.build();
    }

    @Bean(name = STEP_NAME + "1")
    public Step flowStepJob1_step1() {
        return stepBuilderFactory.get(STEP_NAME + "1")
                .tasklet((contribution, chunkContext) -> {
                    log.debug(">> flowStepJob1_step1 <<");
                        return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = STEP_NAME + "2")
    public Step flowStepJob1_step2() {
        return stepBuilderFactory.get(STEP_NAME + "2")
                .tasklet((contribution, chunkContext) -> {
                    log.debug(">> flowStepJob1_step2 <<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
