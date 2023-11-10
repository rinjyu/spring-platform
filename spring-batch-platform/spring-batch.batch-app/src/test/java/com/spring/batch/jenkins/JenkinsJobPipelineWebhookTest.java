package com.spring.batch.jenkins;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

import java.nio.charset.StandardCharsets;

@Slf4j
@Profile("default")
public class JenkinsJobPipelineWebhookTest {

    /**
     * Jenkins Pipeline에서 사용되는 고유 Webhook ID 생성
     * 생성 규칙 : {Jenkins에 생성된 Job의 이름}-({도메인}:{Jenkins에 생성된 Job의 이름}으로 암호화된 값)
     * 예) Jenkins에 생성된 Job의 이름 : sampleWebhookPipeline
     *     도메인 : sample
     *     생성된 Webhook ID : sampleWebhookPipeline-c2FtcGxlOnNhbXBsZVdlYmhvb2tQaXBlbGluZQ
     */
    @Test
    public void generateWebhookId() {

        String domain = "sample";
        String jobName = "sampleInvWebhookPipeline";
        String auth = String.join(":", domain, jobName);
        String encodeAuth = Base64.encodeBase64URLSafeString(auth.getBytes(StandardCharsets.UTF_8));
        String webhookId = String.join("-", jobName, encodeAuth);
        log.info("webhookId = {}", webhookId);
    }
}
