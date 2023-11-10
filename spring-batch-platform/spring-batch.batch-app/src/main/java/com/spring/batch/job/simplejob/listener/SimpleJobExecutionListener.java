package com.spring.batch.job.simplejob.listener;

import com.spring.batch.common.listener.BatchJobExecutionListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.stereotype.Component;


@Component
public class SimpleJobExecutionListener extends BatchJobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        super.beforeJob(jobExecution);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        super.afterJob(jobExecution);
    }
}