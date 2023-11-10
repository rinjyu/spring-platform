package com.spring.batch.job.removejob.tasklet;

import com.spring.batch.common.tasklet.BatchTasklet;
import com.spring.batch.job.removejob.mapper.batch.RemoveHistoryJobMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class RemoveHistoryJobBatchTasklet extends BatchTasklet {

    private final RemoveHistoryJobMapper removeHistoryJobMapper;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {

        /**
         * BATCH_JOB_EXECUTION 테이블의 CREATE_TIME이 현재날짜 기준 90일이 경과된 Spring Batch Meta 데이터 삭제
         * 삭제순서
         *  1. BATCH_STEP_EXECUTION_CONTEXT
         *  2. BATCH_STEP_EXECUTION
         *  3. BATCH_JOB_EXECUTION_CONTEXT
         *  4. BATCH_JOB_EXECUTION_PARMAS
         *  5. BATCH_JOB_EXECUTION
         *  6. BATCH_JOB_INSTANCE
         */
        String removeDateTime = LocalDate.now().minusDays(90L).format(DateTimeFormatter.BASIC_ISO_DATE);

        log.info("=======================");
        log.info("jobName = {}", stepContribution.getStepExecution().getJobExecution().getJobInstance().getJobName());
        log.info("stepName = {}", stepContribution.getStepExecution().getStepName());
        log.info("removeDateTime = {}", removeDateTime);
        log.info("=======================");

        int deletedTotalCount = 0;
        int targetCount = removeHistoryJobMapper.selectRemoveBatchJobExecutionCount(removeDateTime);
        if (targetCount < 1) {
            log.info("삭제할 데이터 없음");
            stepContribution.incrementWriteCount(deletedTotalCount);
            return RepeatStatus.FINISHED;
        }

        int deletedCount = removeHistoryJobMapper.deleteBatchStepExecutionContext(removeDateTime);
        log.info("deletedCount = {}", deletedCount);
        deletedTotalCount += deletedCount;

        deletedCount += removeHistoryJobMapper.deleteBatchStepExecution(removeDateTime);
        log.info("BATCH_STEP_EXECUTION deleted count = {}", deletedCount);
        deletedTotalCount += deletedCount;

        deletedCount += removeHistoryJobMapper.deleteBatchJobExecutionContext(removeDateTime);
        log.info("BATCH_JOB_EXECUTION_CONTEXT deleted count = {}", deletedCount);
        deletedTotalCount += deletedCount;

        deletedCount += removeHistoryJobMapper.deleteBatchJobExecutionParams(removeDateTime);
        log.info("BATCH_JOB_EXECUTION_PARAMS deleted count = {}", deletedCount);
        deletedTotalCount += deletedCount;

        deletedCount += removeHistoryJobMapper.deleteBatchJobExecution(removeDateTime);
        log.info("BATCH_JOB_EXECUTION deleted count = {}", deletedCount);
        deletedTotalCount += deletedCount;

        deletedCount += removeHistoryJobMapper.deleteBatchJobInstance(removeDateTime);
        log.info("BATCH_JOB_INSTANCE deleted count = {}", deletedCount);
        deletedTotalCount += deletedCount;

        log.info("BATCH JOB META DATA total deleted count = {}", deletedTotalCount);

        stepContribution.incrementWriteCount(deletedTotalCount);
        return RepeatStatus.FINISHED;
    }
}
