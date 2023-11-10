package com.spring.batch.job.stepjob;

import com.spring.batch.job.stepjob.tasklet.TaskletStepJobBatchTasklet;
import com.spring.batch.job.stepjob.validator.TaskletStepJobParametersValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class TaskletStepJobConfiguration {

    private static final String JOB_NAME = "taskletStepJob";
    private static final String STEP_NAME = "taskletStepJob_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final TaskletStepJobBatchTasklet taskletStepJobTasklet;

    @Bean(name = JOB_NAME)
    public Job taskletStepJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(taskletStepJob_step())
                .validator(new TaskletStepJobParametersValidator())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step taskletStepJob_step() {
        return stepBuilderFactory.get(STEP_NAME)
                .tasklet(taskletStepJobTasklet)
                .build();
    }
}
