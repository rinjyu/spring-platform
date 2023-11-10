package com.spring.batch.common.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties
@ConfigurationProperties("spring.mail")
public class MailProperties {

    private String host;
    private Integer port;
    private String username;
    private String password;
    private boolean debug;
}
