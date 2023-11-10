package com.spring.batch.job.removejob.mapper.batch;

import com.spring.batch.common.config.datasource.marker.BatchMapper;

@BatchMapper
public interface RemoveHistoryJobMapper {

    int selectRemoveBatchJobExecutionCount(String removeDateTime);
    int deleteBatchStepExecutionContext(String removeDateTime);
    int deleteBatchStepExecution(String removeDateTime);
    int deleteBatchJobExecutionContext(String removeDateTime);
    int deleteBatchJobExecutionParams(String removeDateTime);
    int deleteBatchJobExecution(String removeDateTime);
    int deleteBatchJobInstance(String removeDateTime);
}
