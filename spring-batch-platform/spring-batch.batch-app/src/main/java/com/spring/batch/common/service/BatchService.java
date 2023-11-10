package com.spring.batch.common.service;

import com.spring.batch.common.exception.BatchRunnerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Set;

@Slf4j
@Service
public class BatchService {

    private static String RUN_DATE_TIME_KEY = "run.dateTime";

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final JobRegistry jobRegistry;
    private final JobOperator jobOperator;

    @Autowired
    public BatchService(JobLauncher jobLauncher, JobExplorer jobExplorer, JobRegistry jobRegistry, JobOperator jobOperator) {
        this.jobLauncher = jobLauncher;
        this.jobExplorer = jobExplorer;
        this.jobRegistry = jobRegistry;
        this.jobOperator = jobOperator;
    }

    public void startBatchJob(String jobNames, String... args) {
        for (String jobName : jobNames.split(",")) {
            log.info(">>> {} Start <<<", jobName);
            try {
                Job job = getJob(jobName);
                JobParameters jobParameters = generateJobParameters(job, convertJobParameters(args));

                JobExecution jobExecution = jobLauncher.run(job, jobParameters);

                BatchStatus batchStatus = jobExecution.getStatus();
                log.info("[startBatchJob] {} batchStatus ::: {}", jobName, batchStatus.name());

                if (batchStatus != BatchStatus.COMPLETED) {
                    throw new BatchRunnerException(String.format("명령 수행 중 오류가 발생했습니다. (%s)", batchStatus.name()));
                }
            } catch (Exception e) {
                log.error("[startBatchJob] {} error ::: {}", jobName, e.getMessage());
                throw new BatchRunnerException(e.getMessage());
            }
        }
    }

    public void stopBatchJob(String jobNames) {
        for (String jobName : jobNames.split(",")) {
            log.info(">>> {} Stop <<<", jobName);
            Set<JobExecution> jobExecutions;
            try {
                jobExecutions = jobExplorer.findRunningJobExecutions(jobName);
                for (JobExecution jobExecution : jobExecutions) {
                    if (jobExecution.getStatus() == BatchStatus.STARTED || jobExecution.getStatus() == BatchStatus.STARTING) {
                        jobOperator.stop(jobExecution.getId());
                    }
                }
            } catch (Exception e) {
                log.error("[stopBatchJob] {} error ::: {}", jobName, e.getMessage());
                throw new BatchRunnerException(e.getMessage());
            }
        }
    }

    public void restartBatchJob(String jobNames) {
        for (String jobName : jobNames.split(",")) {
            log.info(">>> {} restart <<<", jobName);
            try {
                JobExecution jobExecution = getJobExecution(jobName);
                Long id = jobOperator.restart(jobExecution.getJobId());
                JobExecution completedJobExecution = jobExplorer.getJobExecution(id);

                BatchStatus batchStatus = completedJobExecution.getStatus();
                log.info("[restartBatchJob] {} batchStatus ::: {}", jobName, batchStatus.name());

                if (batchStatus != BatchStatus.COMPLETED) {
                    throw new BatchRunnerException(String.format("명령 수행 중 오류가 발생했습니다. (%s)", batchStatus.name()));
                }
            } catch (Exception e) {
                log.error("[restartBatchJob] {} error ::: {}", jobName, e.getMessage());
                throw new BatchRunnerException(e.getMessage());
            }
        }
    }

    private Job getJob(String jobName) throws NoSuchJobException {
        return jobRegistry.getJob(jobName);
    }

    private JobExecution getJobExecution(String jobName) {
        JobInstance jobInstance = jobExplorer.getLastJobInstance(jobName);
        JobExecution jobExecution = jobExplorer.getLastJobExecution(jobInstance);
        return jobExecution;
    }

    private JobParameters convertJobParameters(String... args) {
        Properties properties = StringUtils.splitArrayElementsIntoProperties(args, "=");
        JobParameters jobParameters = new DefaultJobParametersConverter().getJobParameters(properties);
        return jobParameters;
    }

    private JobParameters generateJobParameters(Job job, JobParameters jobParameters) {
        jobParameters = new JobParametersBuilder(ObjectUtils.isEmpty(jobParameters) ? new JobParameters() : jobParameters)
                .addString(RUN_DATE_TIME_KEY, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                .toJobParameters();

        if (!ObjectUtils.isEmpty(job.getJobParametersIncrementer())) {
            jobParameters = new JobParametersBuilder(jobParameters, jobExplorer)
                    .getNextJobParameters(job)
                    .toJobParameters();
        }
        return jobParameters;
    }
}
