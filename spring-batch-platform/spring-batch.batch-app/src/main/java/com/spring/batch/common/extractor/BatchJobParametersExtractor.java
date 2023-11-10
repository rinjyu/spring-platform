package com.spring.batch.common.extractor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;

public class BatchJobParametersExtractor extends DefaultJobParametersExtractor {

    public BatchJobParametersExtractor() {
        super();
    }

    @Override
    public void setKeys(String[] keys) {
        super.setKeys(keys);
    }

    @Override
    public JobParameters getJobParameters(Job job, StepExecution stepExecution) {
        return super.getJobParameters(job, stepExecution);
    }

    @Override
    public void setUseAllParentParameters(boolean useAllParentParameters) {
        super.setUseAllParentParameters(useAllParentParameters);
    }
}
