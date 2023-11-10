package com.spring.batch.job.jenkinsjob;

import com.spring.batch.common.adapter.BatchItemWriterAdapter;
import com.spring.batch.common.incrementer.BatchJobParametersIncrementer;
import com.spring.batch.common.mapping.ExcelBeanWrapperRowMapper;
import com.spring.batch.job.jenkinsjob.domain.JenkinsJob;
import com.spring.batch.job.jenkinsjob.service.JenkinsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JenkinsModifyJobConfiguration {

    private static final String JOB_NAME = "jenkinsModifyJob";
    private static final String STEP_NAME = "jenkinsModifyJob_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JenkinsService jenkinsService;

    @Bean(name = JOB_NAME)
    public Job jenkinsModifyJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(jenkinsModifyJob_step())
                .incrementer(new BatchJobParametersIncrementer())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step jenkinsModifyJob_step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<JenkinsJob, JenkinsJob>chunk(20)
                .reader(jenkinsModifyJobItemReader(null))
                .processor((ItemProcessor<JenkinsJob, JenkinsJob>) item -> {
                    //item.generateCommand();
                    log.info("item = {}", item);
                    return item;
                })
                .writer(jenkinsModifyJobItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemStreamReader<JenkinsJob> jenkinsModifyJobItemReader(@Value("#{jobParameters['filePath']}") String filePath) {
        PoiItemReader<JenkinsJob> poiItemReader = new PoiItemReader<>();
        poiItemReader.setResource(new FileSystemResource(filePath));
        poiItemReader.setLinesToSkip(1);
        poiItemReader.setRowMapper(jenkinsModifyJobRowMapper());
        return poiItemReader;
    }

    @Bean
    public ExcelBeanWrapperRowMapper<JenkinsJob> jenkinsModifyJobRowMapper() {
        ExcelBeanWrapperRowMapper<JenkinsJob> excelBeanWrapperRowMapper = new ExcelBeanWrapperRowMapper<>();
        excelBeanWrapperRowMapper.setTargetType(JenkinsJob.class);
        return excelBeanWrapperRowMapper;
    }

    @Bean
    public BatchItemWriterAdapter<JenkinsJob> jenkinsModifyJobItemWriter() {
        BatchItemWriterAdapter<JenkinsJob> batchItemWriterAdapter = new BatchItemWriterAdapter<>();
        batchItemWriterAdapter.setTargetObject(jenkinsService);
        batchItemWriterAdapter.setTargetMethod("modifyJob");
        return batchItemWriterAdapter;
    }
}
