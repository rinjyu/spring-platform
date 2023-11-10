package com.spring.batch.job.stepjob.validator;

import com.spring.batch.common.validator.BatchJobParametersValidator;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

@Slf4j
public class TaskletStepJobParametersValidator extends BatchJobParametersValidator {

    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        super.validate(jobParameters);

        jobParameters.getParameters().forEach((key, value) -> log.debug("{} = {}", key, value));

        String itemId = jobParameters.getString("itemId");
        String unitItemId = jobParameters.getString("unitItemId");
        if (StringUtils.isBlank(itemId)) {
            throw new JobParametersInvalidException("Job Parameter [itemId] 값이 없습니다.");
        }
        if (StringUtils.isBlank(unitItemId)) {
            throw new JobParametersInvalidException("Job Parameter [unitItemId] 값이 없습니다.");
        }
    }
}
