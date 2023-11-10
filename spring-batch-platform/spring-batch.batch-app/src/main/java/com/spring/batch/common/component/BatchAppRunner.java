package com.spring.batch.common.component;

import com.spring.batch.common.service.BatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchAppRunner implements CommandLineRunner, ExitCodeGenerator {

    private int exitCode;
    private final BatchService batchService;

    @Override
    public void run(String... args) {

        if (ObjectUtils.isEmpty(args)) {
            return;
        }

        String command = args[0];
        String jobName = args[1];

        try {
            if ("start".equals(command)) {
                batchService.startBatchJob(jobName, args);
            } else if ("stop".equals(command)) {
                batchService.stopBatchJob(jobName);
            } else if ("restart".equals(command)) {
                batchService.restartBatchJob(jobName);
            } else {
                log.error("실행할 수 없는 명령입니다.");
                this.exitCode = 1;
            }
        } catch (Exception e) {
            log.error("[BatchAppRunner] error = {}", e.getMessage());
            this.exitCode = 1;
        }
        return;
    }

    @Override
    public int getExitCode() {
        return this.exitCode;
    }
}
