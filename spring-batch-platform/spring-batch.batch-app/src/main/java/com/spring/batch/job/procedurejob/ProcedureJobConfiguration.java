package com.spring.batch.job.procedurejob;

import com.spring.batch.common.tasklet.BatchTasklet;
import com.spring.batch.job.common.domain.Inventory;
import com.spring.batch.job.common.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ProcedureJobConfiguration {

    private static final String JOB_NAME = "procedureJob";
    private static final String STEP_NAME = "procedureJob_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final InventoryService inventoryService;

    @Bean(name = JOB_NAME)
    public Job procedureJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(procedureJob_step(null, null, null))
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean(name = STEP_NAME)
    @JobScope
    public Step procedureJob_step(@Value("#{jobParameters['itemId']}") String itemId,
                                    @Value("#{jobParameters['unitItemId']}") String unitItemId,
                                    @Value("#{jobParameters['modifierId']}") String modifierId) {
        return stepBuilderFactory.get(STEP_NAME)
                .tasklet(new BatchTasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        Inventory inventory = Inventory.builder()
                                .itemId(itemId)
                                .unitItemId(unitItemId)
                                .modifierId(modifierId)
                                .build();
                        try {
                            inventoryService.callSpInventoryUsableQtyUpd(inventory);
                        } catch (Exception e) {
                            log.error("procedureJob_step = {}", e.getMessage());
                            throw new RuntimeException("프로시저 수행 중 오류가 발생했습니다.");
                        }
                        return super.execute(stepContribution, chunkContext);
                    }
                })
                .build();
    }
}