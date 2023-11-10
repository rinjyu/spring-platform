package com.spring.batch.job.flowjob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
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
public class SimpleFlowJobConfiguration3 {

    private static final String JOB_NAME = "simpleFlowJob3";
    private static final String FLOW_NAME = "simpleFlowJob3_flow";
    private static final String STEP_NAME = "simpleFlowJob3_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job simpleFlowJob3() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(simpleFlowJob3_flow1())
                .on(BatchStatus.COMPLETED.name())
                .to(simpleFlowJob3_flow2())
                .from(simpleFlowJob3_flow1())
                .on(BatchStatus.FAILED.name())
                .to(simpleFlowJob3_flow3())
                .end()
                .build();
    }

    @Bean(name = FLOW_NAME + "1")
    public Flow simpleFlowJob3_flow1() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>(FLOW_NAME + "1");
        flowBuilder.start(simpleFlowJob3_step1())
                .next(simpleFlowJob3_step2())
                .end();
        return flowBuilder.build();
    }

    @Bean(name = FLOW_NAME + "2")
    public Flow simpleFlowJob3_flow2() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>(FLOW_NAME + "2");
        flowBuilder.start(simpleFlowJob3_flow3())
                .next(simpleFlowJob3_step3())
                .next(simpleFlowJob3_step4())
                .end();
        return flowBuilder.build();
    }

    @Bean(name = FLOW_NAME + "3")
    public Flow simpleFlowJob3_flow3() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>(FLOW_NAME + "3");
        flowBuilder.start(simpleFlowJob3_step5())
                .next(simpleFlowJob3_step6())
                .end();
        return flowBuilder.build();
    }

    @Bean(name = STEP_NAME + "1")
    public Step simpleFlowJob3_step1() {
        return stepBuilderFactory.get(STEP_NAME + "1")
                .tasklet((contribution, chunkContext) -> {
                    log.debug(">> simpleFlowJob3_step1 <<");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean(name = STEP_NAME + "2")
    public Step simpleFlowJob3_step2() {
        return stepBuilderFactory.get(STEP_NAME + "2")
                .tasklet((contribution, chunkContext) -> {
                    log.debug(">> simpleFlowJob3_step2 <<");
                    throw new RuntimeException("simpleFlowJob3_step2 is failed.");
                }).build();
    }

    @Bean(name = STEP_NAME + "3")
    public Step simpleFlowJob3_step3() {
        return stepBuilderFactory.get(STEP_NAME + "3")
                .tasklet((contribution, chunkContext) -> {
                    log.debug(">> simpleFlowJob3_step3 <<");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean(name = STEP_NAME + "4")
    public Step simpleFlowJob3_step4() {
        return stepBuilderFactory.get(STEP_NAME + "4")
                .tasklet((contribution, chunkContext) -> {
                    log.debug(">> simpleFlowJob3_step4 <<");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean(name = STEP_NAME + "5")
    public Step simpleFlowJob3_step5() {
        return stepBuilderFactory.get(STEP_NAME + "5")
                .tasklet((contribution, chunkContext) -> {
                    log.debug(">> simpleFlowJob3_step5 <<");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean(name = STEP_NAME + "6")
    public Step simpleFlowJob3_step6() {
        return stepBuilderFactory.get(STEP_NAME + "6")
                .tasklet((contribution, chunkContext) -> {
                    log.debug(">> simpleFlowJob3_step6 <<");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
