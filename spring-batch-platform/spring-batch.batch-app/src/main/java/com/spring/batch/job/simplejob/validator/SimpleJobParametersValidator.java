package com.spring.batch.job.simplejob.validator;

import com.spring.batch.common.validator.BatchJobParametersValidator;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

@Slf4j
public class SimpleJobParametersValidator extends BatchJobParametersValidator {

    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        super.validate(jobParameters);

        jobParameters.getParameters().forEach((key, value) -> log.debug("{} = {}", key, value));

        String requestDate = jobParameters.getString("requestDate");
        String runner = jobParameters.getString("runner");
        if (StringUtils.isBlank(requestDate)) {
            throw new JobParametersInvalidException("Job Parameter [requestDate] 값이 없습니다.");
        }
        if (requestDate.length() != 8) {
            throw new JobParametersInvalidException("Job Parameter [requestDate] 값의 길이가 유효하지 않습니다.");
        }
        if (StringUtils.isBlank(runner)) {
            throw new JobParametersInvalidException("Job Parameter [runner] 값이 없습니다.");
        }
    }
}
