FROM jenkins/jenkins:lts-jdk11
MAINTAINER rinjyu <rinjyu@naver.com>

USER jenkins:jenkins

RUN mkdir -p ./jenkins_home
RUN chmod -R 777 ./jenkins_home
RUN mkdir -p /webapp/app

WORKDIR ./jenkins_home