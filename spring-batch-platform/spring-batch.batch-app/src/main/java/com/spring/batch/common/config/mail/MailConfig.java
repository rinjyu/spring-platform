package com.spring.batch.common.config.mail;

import com.spring.batch.common.property.MailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    private final MailProperties mailProperties;

    @Value("${spring.mail.transport.protocol}")
    private String transportProtocol;

    @Value("${spring.mail.properties.smtp.auth}")
    private String smtpAuth;

    @Value("${spring.mail.properties.smtp.starttls.enable}")
    private String smtpStarttlsEnable;

    @Value("${spring.mail.debug}")
    private String mailDebug;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailProperties.getHost());
        javaMailSender.setPort(mailProperties.getPort());
        javaMailSender.setUsername(mailProperties.getUsername());
        javaMailSender.setPassword(mailProperties.getPassword());
        javaMailSender.setJavaMailProperties(getMailProperties());
        javaMailSender.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", transportProtocol);
        properties.setProperty("mail.smtp.auth", smtpAuth);
        properties.setProperty("mail.smtp.starttls.enable", smtpStarttlsEnable);
        properties.setProperty("mail.debug", mailDebug);
        return properties;
    }
}
