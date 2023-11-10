package com.spring.batch.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BatchStepExecutionListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        log.info(">>> stepName {} ::: 실행 전 <<<", stepName);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        ExitStatus exitStatus = stepExecution.getExitStatus();

        log.info(">>> stepName {} ::: 실행 결과 <<<", stepName);
        log.info(">>> exitStatus ::: {} <<<", exitStatus.getExitCode());

        switch (exitStatus.getExitCode()) {
            case "FAILED":
                log.info(">>> stepName {} 수행에 실패했습니다. 담당자 확인바랍니다.", stepName);
                break;
            case "COMPLETED":
                log.info(">>> stepName {} 수행이 완료되었습니다.", stepName);
                break;
            default:
                log.info(">>> stepName {} 수행 완료, exitStatus : {}", stepName, exitStatus);
                break;
        }
        return exitStatus;
    }
}
