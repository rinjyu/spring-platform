package com.spring.batch.common.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BatchTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.debug("=======================");
        log.debug("jobName = {}", stepContribution.getStepExecution().getJobExecution().getJobInstance().getJobName());
        log.debug("stepName = {}", stepContribution.getStepExecution().getStepName());
        log.debug("=======================");

        return null;
    }
}
