package com.spring.batch.job.jenkinsjob.service;

import com.spring.batch.job.jenkinsjob.domain.JenkinsJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class JenkinsService {

    private final RestTemplate restTemplate;
    private static final String JENKINS_END_POINT = "http://127.0.0.1:9090";
    private static final String JENKINS_USERNAME = "JENKINS_USERNAME";
    private static final String JENKINS_PASSWORD = "JENKINS_PASSWORD";

    // JENKINS_CRUMB값은 http://{젠킨스 아이피}:{포트}/crumbIssuer/api/json 통해 확인 가능함(일정 주기로 값이 변경됨)
    private static final String JENKINS_CRUMB = "JENKINS_CRUMB";

    public void createJob(JenkinsJob dto) {

        log.info("createJenkinsJob = {}", dto);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(JENKINS_END_POINT)
                .path("/createItem")
                .queryParam("name", dto.getJobName())
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_XML);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
        httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
        httpHeaders.set("Jenkins-Crumb", JENKINS_CRUMB);
        httpHeaders.setBasicAuth(JENKINS_USERNAME, JENKINS_PASSWORD);
        HttpEntity<String> httpEntity = new HttpEntity<>(getJobConfigXml(dto), httpHeaders);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(uriComponents.toUriString(), HttpMethod.POST, httpEntity, String.class);
            log.info("statusCode = {}", responseEntity.getStatusCodeValue());
        } catch (RestClientException e) {
            log.error("[JenkinsService ::: createJob] error ={}", e.getMessage());
            throw new RuntimeException("Jenkins Job 등록 오류 발생");
        }
    }

    public void modifyJob(JenkinsJob dto) throws UnsupportedEncodingException {

        log.info("createJenkinsJob = {}", dto);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(JENKINS_END_POINT)
                .path("/job/{jobName}/config.xml")
                .buildAndExpand(dto.getJobName());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_XML);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
        httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
        httpHeaders.set("Jenkins-Crumb", JENKINS_CRUMB);
        httpHeaders.setBasicAuth(JENKINS_USERNAME, JENKINS_PASSWORD);
        HttpEntity<String> httpEntity = new HttpEntity<>(getJobConfigXml(dto), httpHeaders);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(uriComponents.toUriString(), HttpMethod.POST, httpEntity, String.class);
            log.info("statusCode = {}", responseEntity.getStatusCodeValue());
        } catch (RestClientException e) {
            log.error("[JenkinsService ::: modifyJob] error ={}", e.getMessage());
            throw new RuntimeException("Jenkins Job 수정 오류 발생");
        }
    }

    private static String getJobConfigXml(JenkinsJob dto) {
        StringBuilder xmlConfig = new StringBuilder();
        xmlConfig.append("<?xml version=\"1.1\" encoding=\"UTF-8\"?>\n");
        xmlConfig.append("<project>\n");
        xmlConfig.append("  <description>" + dto.getDescription() + "</description>\n");
        xmlConfig.append("  <keepDependencies>false</keepDependencies>\n");
        xmlConfig.append("  <properties>\n");
        xmlConfig.append("    <org.jenkinsci.plugins.blockbuildfinalproject.BlockBuildJobProperty plugin=\"block-build-final-project@1.0\">\n");
        xmlConfig.append("      <useBlockBuildUpstreamProject>false</useBlockBuildUpstreamProject>\n");
        xmlConfig.append("      <useBlockBuildDownstreamProject>false</useBlockBuildDownstreamProject>\n");
        xmlConfig.append("      <finalUpstreamProjectsList/>\n");
        xmlConfig.append("      <finalDownstreamProjectsList reference=\"../finalUpstreamProjectsList\"/>\n");
        xmlConfig.append("    </org.jenkinsci.plugins.blockbuildfinalproject.BlockBuildJobProperty>\n");
        xmlConfig.append("    <jenkins.model.BuildDiscarderProperty>\n");
        xmlConfig.append("      <strategy class=\"hudson.tasks.LogRotator\">\n");
        xmlConfig.append("        <daysToKeep>" + dto.getDaysToKeep() + "</daysToKeep>\n");
        xmlConfig.append("        <numToKeep>" + dto.getNumToKeep() + "</numToKeep>\n");
        xmlConfig.append("        <artifactDaysToKeep>-1</artifactDaysToKeep>\n");
        xmlConfig.append("        <artifactNumToKeep>-1</artifactNumToKeep>\n");
        xmlConfig.append("      </strategy>\n");
        xmlConfig.append("    </jenkins.model.BuildDiscarderProperty>\n");
        xmlConfig.append("  </properties>\n");
        xmlConfig.append("  <scm class=\"hudson.scm.NullSCM\"/>\n");
        xmlConfig.append("  <canRoam>true</canRoam>\n");
        xmlConfig.append("  <disabled>" + ("Y".equals(dto.getDisabled()) ? true : false) + "</disabled>\n");
        xmlConfig.append("  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>\n");
        xmlConfig.append("  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>\n");
        xmlConfig.append("  <jdk>" + dto.getJdk() + "</jdk>\n");

        if (StringUtils.isNotBlank(dto.getSchedule())) {
            xmlConfig.append("  <triggers>\n");
            xmlConfig.append("      <hudson.triggers.TimerTrigger>\n");
            xmlConfig.append("        <spec>" + dto.getSchedule() + "</spec>\n");
            xmlConfig.append("      </hudson.triggers.TimerTrigger>\n");
            xmlConfig.append("  </triggers>\n");
        }

        xmlConfig.append("  <concurrentBuild>false</concurrentBuild>\n");
        xmlConfig.append("  <builders>\n");
        xmlConfig.append("    <hudson.tasks.Shell>\n");
        xmlConfig.append("      <command>" + dto.getCommand() + "</command>\n");
        xmlConfig.append("      <configuredLocalRules/>\n");
        xmlConfig.append("    </hudson.tasks.Shell>\n");
        xmlConfig.append("  </builders>\n");
        xmlConfig.append("  <publishers/>\n");
        xmlConfig.append("  <buildWrappers>\n");
        xmlConfig.append("    <hudson.plugins.timestamper.TimestamperBuildWrapper plugin=\"timestamper@1.23\"/>\n");
        xmlConfig.append("  </buildWrappers>\n");
        xmlConfig.append("</project>");
        return xmlConfig.toString();
    }
}