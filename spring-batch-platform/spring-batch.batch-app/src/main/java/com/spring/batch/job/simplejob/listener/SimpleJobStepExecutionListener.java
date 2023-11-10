package com.spring.batch.job.simplejob.listener;

import com.spring.batch.common.listener.BatchStepExecutionListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

@Component
public class SimpleJobStepExecutionListener extends BatchStepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        super.beforeStep(stepExecution);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return super.afterStep(stepExecution);
    }
}
