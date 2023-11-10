package com.spring.batch.job.stepjob;

import com.spring.batch.common.extractor.BatchJobParametersExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobStepJobConfiguration {

    private static final String JOB_PARENT_NAME = "jobStepParentJob";
    private static final String JOB_CHILD_NAME = "jobStepChildJob";
    private static final String STEP_PARENT_NAME = "jobStepParentJob_step";
    private static final String STEP_CHILD_NAME = "jobStepChildJob_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_PARENT_NAME)
    public Job jobStepParentJob() {
        return jobBuilderFactory.get(JOB_PARENT_NAME)
                .start(jobStepParentJob_step1(null))
                .next(jobStepParentJob_step2())
                .build();
    }

    @Bean(name = STEP_PARENT_NAME + "1")
    public Step jobStepParentJob_step1(JobLauncher jobLauncher) {
        BatchJobParametersExtractor extractor = new BatchJobParametersExtractor();
        extractor.setKeys(new String[]{"message"});
        return stepBuilderFactory.get(STEP_PARENT_NAME + "1")
                .job(jobStepChildJob())
                .parametersExtractor(extractor)
                .launcher(jobLauncher)
                .build();
    }

    @Bean(name = STEP_PARENT_NAME + "2")
    public Step jobStepParentJob_step2() {
        return stepBuilderFactory.get(STEP_PARENT_NAME + "2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean(name = JOB_CHILD_NAME)
    public Job jobStepChildJob() {
        return jobBuilderFactory.get(JOB_CHILD_NAME)
                .start(jobStepChildJob_step(null))
                .build();
    }

    @JobScope
    @Bean(name = STEP_CHILD_NAME)
    public Step jobStepChildJob_step(@Value("#{jobParameters['message']}") String message) {
        return stepBuilderFactory.get(STEP_CHILD_NAME)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        log.debug("message = {}", message);
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
}
