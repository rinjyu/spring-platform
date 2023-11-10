package com.spring.batch.job.jenkinsjob.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class JenkinsJob {

    private String jobName;
    private String schedule;
    private String shellFile;
    private String parameter;
    private String command;
    private String jdk;
    private int daysToKeep;
    private int numToKeep;
    private String disabled;
    private String description;

    public void generateCommand() {
        this.command = String.format("%s %s", this.getShellFile(), this.getParameter());
    }
}
