-- ###################################
--  Spring Batch
-- ###################################
CREATE TABLE BATCH_JOB_INSTANCE (
  JOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY,
  VERSION BIGINT,
  JOB_NAME VARCHAR(100) NOT NULL,
  JOB_KEY VARCHAR(32) NOT NULL,
  constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
);

CREATE TABLE BATCH_JOB_EXECUTION (
  JOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY,
  VERSION BIGINT,
  JOB_INSTANCE_ID BIGINT NOT NULL,
  CREATE_TIME TIMESTAMP NOT NULL,
  START_TIME TIMESTAMP DEFAULT NULL,
  END_TIME TIMESTAMP DEFAULT NULL,
  STATUS VARCHAR(10),
  EXIT_CODE VARCHAR(2500),
  EXIT_MESSAGE VARCHAR(2500),
  LAST_UPDATED TIMESTAMP,
  JOB_CONFIGURATION_LOCATION VARCHAR(2500) NULL,
  constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
  references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
);

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS (
  JOB_EXECUTION_ID BIGINT NOT NULL,
  TYPE_CD VARCHAR(6) NOT NULL,
  KEY_NAME VARCHAR(100) NOT NULL,
  STRING_VAL VARCHAR(250),
  DATE_VAL TIMESTAMP DEFAULT NULL,
  LONG_VAL BIGINT,
  DOUBLE_VAL DOUBLE PRECISION,
  IDENTIFYING CHAR(1) NOT NULL,
  constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
  references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

CREATE TABLE BATCH_STEP_EXECUTION (
  STEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY,
  VERSION BIGINT NOT NULL,
  STEP_NAME VARCHAR(100) NOT NULL,
  JOB_EXECUTION_ID BIGINT NOT NULL,
  START_TIME TIMESTAMP NOT NULL,
  END_TIME TIMESTAMP DEFAULT NULL,
  STATUS VARCHAR(10),
  COMMIT_COUNT BIGINT,
  READ_COUNT BIGINT,
  FILTER_COUNT BIGINT,
  WRITE_COUNT BIGINT,
  READ_SKIP_COUNT BIGINT,
  WRITE_SKIP_COUNT BIGINT,
  PROCESS_SKIP_COUNT BIGINT,
  ROLLBACK_COUNT BIGINT,
  EXIT_CODE VARCHAR(2500),
  EXIT_MESSAGE VARCHAR(2500),
  LAST_UPDATED TIMESTAMP,
  constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
  references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT (
  STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
  SHORT_CONTEXT VARCHAR(2500) NOT NULL,
  SERIALIZED_CONTEXT TEXT,
  constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
  references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
);

CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT (
  JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
  SHORT_CONTEXT VARCHAR(2500) NOT NULL,
  SERIALIZED_CONTEXT TEXT,
  constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
  references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE;

CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE;

CREATE SEQUENCE BATCH_JOB_SEQ MAXVALUE 9223372036854775807 NO CYCLE;

-- ###################################
--  Spring Cloud Config Server
-- ###################################
CREATE TABLE BATCH_CONFIG_PROPERTIES (
                                        ID SERIAL PRIMARY KEY,
                                        APPLICATION VARCHAR(500) NOT NULL,
                                        PROFILE VARCHAR(30) NOT NULL,
                                        LABEL VARCHAR(30),
                                        PROPERTY_KEY TEXT NOT NULL,
                                        PROPERTY_VALUE TEXT NOT NULL,
                                        REG_DTS TIMESTAMP NOT NULL,
                                        REG_ID VARCHAR(20) NOT NULL,
                                        MOD_DTS TIMESTAMP NOT NULL,
                                        MOD_ID VARCHAR(20) NOT NULL
);

CREATE SEQUENCE SEQ_BATCH_CONFIG_PROPERTIES MAXVALUE 9223372036854775807 NO CYCLE;

INSERT INTO BATCH_CONFIG_PROPERTIES (ID, APPLICATION, PROFILE, LABEL, PROPERTY_KEY, PROPERTY_VALUE, REG_DTS, MOD_DTS, REG_ID, MOD_ID) VALUES
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.batch-manage.datasource.jdbc-url', 'jdbc:log4jdbc:postgresql://localhost:5432/batchdb?useUnicode=true&characterEncoding=utf8', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.batch-manage.datasource.username', 'batch', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.batch-manage.datasource.password', 'batch', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.batch-manage.datasource.driver-class-name', 'net.sf.log4jdbc.sql.jdbcapi.DriverSpy', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.batch-manage.datasource.type', 'com.zaxxer.hikari.HikariDataSource', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.batch-manage.datasource.hikari.minimum-idle', '2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.batch-manage.datasource.hikari.maximum-pool-size', '10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.batch-manage.datasource.hikari.connection-test-query', 'SELECT 1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.batch-manage.datasource.mybatis.config', 'classpath:mybatis/config/mybatis-config.xml', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.batch-manage.datasource.mybatis.mapper-locations', 'classpath*:com/spring/batch/**/mapper/batch/*Mapper.xml', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.postgres.datasource.jdbc-url', 'jdbc:log4jdbc:postgresql://localhost:5432/batchdb?useUnicode=true&characterEncoding=utf8&escapeSyntaxCallMode=callIfNoReturn', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.postgres.datasource.username', 'batch', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.postgres.datasource.password', 'batch', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.postgres.datasource.driver-class-name', 'net.sf.log4jdbc.sql.jdbcapi.DriverSpy', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.postgres.datasource.type', 'com.zaxxer.hikari.HikariDataSource', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.postgres.datasource.hikari.minimum-idle', '2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'spring-batch.batch-app', 'default', 'master', 'spring.postgres.datasource.hikari.maximum-pool-size', '10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'ssg-guide.batch-app', 'default', 'master', 'spring.postgres.datasource.hikari.connection-test-query', 'SELECT 1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'ssg-guide.batch-app', 'default', 'master', 'spring.postgres.datasource.mybatis.config', 'classpath:mybatis/config/mybatis-config.xml', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'ssg-guide.batch-app', 'default', 'master', 'spring.postgres.datasource.mybatis.mapper-locations', 'classpath*:com/spring/batch/**/mapper/postgres/*Mapper.xml', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'ssg-guide.batch-app', 'default', 'master', 'spring.oracle.datasource.jdbc-url', 'jdbc:log4jdbc:oracle:thin:@127.0.0.1:1538:orcl', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'ssg-guide.batch-app', 'default', 'master', 'spring.oracle.datasource.username', 'oracle', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'ssg-guide.batch-app', 'default', 'master', 'spring.oracle.datasource.password', 'oracle', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'ssg-guide.batch-app', 'default', 'master', 'spring.oracle.datasource.driver-class-name', 'net.sf.log4jdbc.sql.jdbcapi.DriverSpy', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'ssg-guide.batch-app', 'default', 'master', 'spring.oracle.datasource.type', 'com.zaxxer.hikari.HikariDataSource', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'ssg-guide.batch-app', 'default', 'master', 'spring.oracle.datasource.hikari.minimum-idle', '2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'ssg-guide.batch-app', 'default', 'master', 'spring.oracle.datasource.hikari.maximum-pool-size', '10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'ssg-guide.batch-app', 'default', 'master', 'spring.oracle.datasource.hikari.connection-test-query', 'SELECT 1 FROM DUAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'ssg-guide.batch-app', 'default', 'master', 'spring.oracle.datasource.mybatis.config', 'classpath:mybatis/config/mybatis-config.xml', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'ssg-guide.batch-app', 'default', 'master', 'spring.oracle.datasource.mybatis.mapper-locations', 'classpath*:com/spring/batch/**/mapper/oracle/*Mapper.xml', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'ssg-guide.batch-app', 'default', 'master', 'spring.batch.job.names', 'NONE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(NEXTVAL('SEQ_BATCH_CONFIG_PROPERTIES'), 'ssg-guide.batch-app', 'default', 'master', 'spring.batch.jdbc.initialize-schema', 'never', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin');

COMMIT;