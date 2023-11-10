package com.spring.batch.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BatchJobExecutionListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(">>> jobName ::: {} 실행 전 <<<", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        BatchStatus batchStatus = jobExecution.getStatus();
        log.info(">>> jobName ::: {} 실행 결과 <<<", jobName);
        log.info(">>> batchStatus ::: {} <<<", batchStatus);
        log.info(">>> 시작일자 ::: {}", jobExecution.getStartTime());
        log.info(">>> 종료일자 ::: {}", jobExecution.getEndTime());
        log.info(">>> 총 소요 시간 ::: {}", jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime());
        switch (batchStatus) {
            case FAILED:
                log.info(">>> jobName ::: {} 수행에 실패했습니다. 담당자 확인바랍니다.", jobName);
                break;
            case COMPLETED:
                log.info(">>> jobName ::: {} 수행이 완료되었습니다.", jobName);
                break;
            default:
                log.info(">>> jobName ::: {} 수행 완료, batchStatus : {}", jobName, batchStatus);
                break;
        }
    }
}
