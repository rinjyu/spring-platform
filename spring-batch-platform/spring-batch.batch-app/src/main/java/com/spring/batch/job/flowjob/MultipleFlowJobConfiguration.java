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
public class MultipleFlowJobConfiguration {

    private static final String JOB_NAME = "multipleFlowJob";
    private static final String FLOW_NAME = "multipleFlowJob_flow";
    private static final String STEP_NAME = "multipleFlowJob_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job multipleFlowJob() {
        return this.jobBuilderFactory.get(JOB_NAME)
                .start(multipleFlowJob_flow1())
                .next(multipleFlowJob_step3())
                .next(multipleFlowJob_flow2())
                .next(multipleFlowJob_step6())
                .end()
                .build();

    }

    @Bean(name = FLOW_NAME + "1")
    public Flow multipleFlowJob_flow1() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>(FLOW_NAME + "1");
        flowBuilder.start(multipleFlowJob_step1())
                .next(multipleFlowJob_step2())
                .end();
        return flowBuilder.build();

    }

    @Bean(name = FLOW_NAME + "2")
    public Flow multipleFlowJob_flow2() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>(FLOW_NAME + "2");
        flowBuilder.start(multipleFlowJob_step4())
                .next(multipleFlowJob_step5())
                .end();
        return flowBuilder.build();

    }

    @Bean(name = STEP_NAME + "1")
    public Step multipleFlowJob_step1() {
        return stepBuilderFactory.get(STEP_NAME + "1")
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> multipleFlowJob_step1 <<<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = STEP_NAME + "2")
    public Step multipleFlowJob_step2() {
        return stepBuilderFactory.get(STEP_NAME + "2")
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> multipleFlowJob_step2 <<<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = STEP_NAME + "3")
    public Step multipleFlowJob_step3() {
        return stepBuilderFactory.get(STEP_NAME + "3")
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> multipleFlowJob_step3 <<<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = STEP_NAME + "4")
    public Step multipleFlowJob_step4() {
        return stepBuilderFactory.get(STEP_NAME + "4")
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> multipleFlowJob_step4 <<<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = STEP_NAME + "5")
    public Step multipleFlowJob_step5() {
        return stepBuilderFactory.get(STEP_NAME + "5")
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> multipleFlowJob_step5 <<<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = STEP_NAME + "6")
    public Step multipleFlowJob_step6() {
        return stepBuilderFactory.get(STEP_NAME + "6")
                .tasklet((stepContribution, chunkContext) -> {
                    log.debug(">>> multipleFlowJob_step6 <<<");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}