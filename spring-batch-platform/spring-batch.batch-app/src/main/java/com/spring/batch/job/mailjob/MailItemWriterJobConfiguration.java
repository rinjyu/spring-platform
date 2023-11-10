package com.spring.batch.job.mailjob;

import com.spring.batch.common.mapping.ExcelBeanWrapperRowMapper;
import com.spring.batch.job.mailjob.domain.MailMessage;
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
import org.springframework.batch.item.mail.SimpleMailMessageItemWriter;
import org.springframework.batch.item.mail.builder.SimpleMailMessageItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MailItemWriterJobConfiguration {

    private static final String JOB_NAME = "mailItemWriterJob";
    private static final String STEP_NAME = "mailItemWriterJob_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JavaMailSender javaMailSender;

    @Bean(name = JOB_NAME)
    public Job mailItemWriterJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(mailItemWriterJob_step())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step mailItemWriterJob_step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<MailMessage, SimpleMailMessage>chunk(5)
                .reader(simpleMailExcelItemStreamReader(null))
                .processor((ItemProcessor<MailMessage, SimpleMailMessage>) mailMessage -> {
                    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                    simpleMailMessage.setFrom(mailMessage.getFrom());
                    simpleMailMessage.setTo(mailMessage.getTo());
                    simpleMailMessage.setSubject(mailMessage.getSubject());
                    simpleMailMessage.setText(mailMessage.getText());

                    return simpleMailMessage;
                })
                .writer(simpleMailMessageItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemStreamReader<MailMessage> simpleMailExcelItemStreamReader(@Value("#{jobParameters['filePath']}") String filePath) {
        PoiItemReader<MailMessage> poiItemReader = new PoiItemReader<>();
        poiItemReader.setResource(new FileSystemResource(filePath));
        poiItemReader.setLinesToSkip(1);
        poiItemReader.setRowMapper(simpleMailMessageExcelBeanWrapperRowMapper());
        return poiItemReader;
    }

    @Bean
    public ExcelBeanWrapperRowMapper<MailMessage> simpleMailMessageExcelBeanWrapperRowMapper() {
        ExcelBeanWrapperRowMapper<MailMessage> excelBeanWrapperRowMapper = new ExcelBeanWrapperRowMapper<>();
        excelBeanWrapperRowMapper.setTargetType(MailMessage.class);
        return excelBeanWrapperRowMapper;
    }

    @Bean
    public SimpleMailMessageItemWriter simpleMailMessageItemWriter() {
        return new SimpleMailMessageItemWriterBuilder()
                .mailSender(javaMailSender)
                .build();
    }
}
