-- ###################################
--  Spring Cloud Data Flow
-- ###################################
create sequence if not exists hibernate_sequence start 1 increment 1;

create table app_registration (
  id int8 not null,
  object_version int8,
  default_version boolean,
  metadata_uri text,
  name varchar(255),
  type int4,
  uri text,
  version varchar(255),
  primary key (id)
);

create table task_deployment (
  id int8 not null,
  object_version int8,
  task_deployment_id varchar(255) not null,
  task_definition_name varchar(255) not null,
  platform_name varchar(255) not null,
  created_on timestamp,
  primary key (id)
);

create table audit_records (
  id int8 not null,
  audit_action int8,
  audit_data text,
  audit_operation int8,
  correlation_id varchar(255),
  created_by varchar(255),
  created_on timestamp,
  primary key (id)
);

create table stream_definitions (
  definition_name varchar(255) not null,
  definition text,
  primary key (definition_name)
);

create table task_definitions (
  definition_name varchar(255) not null,
  definition text,
  primary key (definition_name)
);

CREATE TABLE TASK_EXECUTION (
  TASK_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
  START_TIME TIMESTAMP DEFAULT NULL,
  END_TIME TIMESTAMP DEFAULT NULL,
  TASK_NAME  VARCHAR(100),
  EXIT_CODE INTEGER,
  EXIT_MESSAGE VARCHAR(2500),
  ERROR_MESSAGE VARCHAR(2500),
  LAST_UPDATED TIMESTAMP,
  EXTERNAL_EXECUTION_ID VARCHAR(255),
  PARENT_EXECUTION_ID BIGINT
);

CREATE TABLE TASK_EXECUTION_PARAMS (
  TASK_EXECUTION_ID BIGINT NOT NULL,
  TASK_PARAM VARCHAR(2500),
  constraint TASK_EXEC_PARAMS_FK foreign key (TASK_EXECUTION_ID)
  references TASK_EXECUTION(TASK_EXECUTION_ID)
);

CREATE TABLE TASK_TASK_BATCH (
  TASK_EXECUTION_ID BIGINT NOT NULL,
  JOB_EXECUTION_ID BIGINT NOT NULL,
  constraint TASK_EXEC_BATCH_FK foreign key (TASK_EXECUTION_ID)
  references TASK_EXECUTION(TASK_EXECUTION_ID)
);

CREATE SEQUENCE TASK_SEQ MAXVALUE 9223372036854775807 NO CYCLE;

CREATE TABLE TASK_LOCK (
  LOCK_KEY CHAR(36) NOT NULL,
  REGION VARCHAR(100) NOT NULL,
  CLIENT_ID CHAR(36),
  CREATED_DATE TIMESTAMP NOT NULL,
  constraint LOCK_PK primary key (LOCK_KEY, REGION)
);

-- ###################################
--  Spring Cloud Skipper
-- ###################################
create table skipper_app_deployer_data (
  id int8 not null,
  object_version int8,
  deployment_data text,
  release_name varchar(255),
  release_version int4,
  primary key (id)
);

create table skipper_info (
  id int8 not null,
  object_version int8,
  deleted timestamp,
  description varchar(255),
  first_deployed timestamp,
  last_deployed timestamp,
  status_id int8,
  primary key (id)
);

create table skipper_manifest (
  id int8 not null,
  object_version int8,
  data text,
  primary key (id)
);

create table skipper_package_file (
  id int8 not null,
  package_bytes oid,
  primary key (id)
);

create table skipper_package_metadata (
  id int8 not null,
  object_version int8,
  api_version varchar(255),
  description text,
  display_name varchar(255),
  icon_url text,
  kind varchar(255),
  maintainer varchar(255),
  name varchar(255),
  origin varchar(255),
  package_home_url text,
  package_source_url text,
  repository_id int8,
  repository_name varchar(255),
  sha256 varchar(255),
  tags text,
  version varchar(255),
  packagefile_id int8,
  primary key (id)
);

create table skipper_release (
  id int8 not null,
  object_version int8,
  config_values_string text,
  name varchar(255),
  package_metadata_id int8,
  pkg_json_string text,
  platform_name varchar(255),
  repository_id int8,
  version int4 not null,
  info_id int8,
  manifest_id int8,
  primary key (id)
);

create table skipper_repository (
  id int8 not null,
  object_version int8,
  description varchar(255),
  local boolean,
  name varchar(255),
  repo_order int4,
  source_url text,
  url text,
  primary key (id)
);

create table skipper_status (
  id int8 not null,
  platform_status text,
  status_code varchar(255),
  primary key (id)
);

create table action (
  id int8 not null,
  name varchar(255),
  spel varchar(255),
  primary key (id)
);

create table deferred_events (
  jpa_repository_state_id int8 not null,
  deferred_events varchar(255)
);

create table guard (
  id int8 not null,
  name varchar(255),
  spel varchar(255),
  primary key (id)
);

create table state (
  id int8 not null,
  initial_state boolean not null,
  kind int4,
  machine_id varchar(255),
  region varchar(255),
  state varchar(255),
  submachine_id varchar(255),
  initial_action_id int8,
  parent_state_id int8,
  primary key (id)
);

create table state_entry_actions (
  jpa_repository_state_id int8 not null,
  entry_actions_id int8 not null,
  primary key (jpa_repository_state_id, entry_actions_id)
);

create table state_exit_actions (
  jpa_repository_state_id int8 not null,
  exit_actions_id int8 not null,
  primary key (jpa_repository_state_id, exit_actions_id)
);

create table state_state_actions (
  jpa_repository_state_id int8 not null,
  state_actions_id int8 not null,
  primary key (jpa_repository_state_id, state_actions_id)
);

create table state_machine (
  machine_id varchar(255) not null,
  state varchar(255),
  state_machine_context oid,
  primary key (machine_id)
);

create table transition (
  id int8 not null,
  event varchar(255),
  kind int4,
  machine_id varchar(255),
  guard_id int8,
  source_id int8,
  target_id int8,
  primary key (id)
);

create table transition_actions (
  jpa_repository_transition_id int8 not null,
  actions_id int8 not null,
  primary key (jpa_repository_transition_id, actions_id)
);

create index idx_pkg_name on skipper_package_metadata (name);

create index idx_rel_name on skipper_release (name);

create index idx_repo_name on skipper_repository (name);

alter table skipper_repository
  add constraint uk_repository unique (name);

alter table deferred_events
  add constraint fk_state_deferred_events
  foreign key (jpa_repository_state_id)
  references state;

alter table skipper_info
  add constraint fk_info_status
  foreign key (status_id)
  references skipper_status;

alter table skipper_package_metadata
  add constraint fk_package_metadata_pfile
  foreign key (packagefile_id)
  references skipper_package_file;

alter table skipper_release
  add constraint fk_release_info
  foreign key (info_id)
  references skipper_info;

alter table skipper_release
  add constraint fk_release_manifest
  foreign key (manifest_id)
  references skipper_manifest;

alter table state
  add constraint fk_state_initial_action
  foreign key (initial_action_id)
  references action;

alter table state
  add constraint fk_state_parent_state
  foreign key (parent_state_id)
  references state;

alter table state_entry_actions
  add constraint fk_state_entry_actions_a
  foreign key (entry_actions_id)
  references action;

alter table state_entry_actions
  add constraint fk_state_entry_actions_s
  foreign key (jpa_repository_state_id)
  references state;

alter table state_exit_actions
  add constraint fk_state_exit_actions_a
  foreign key (exit_actions_id)
  references action;

alter table state_exit_actions
  add constraint fk_state_exit_actions_s
  foreign key (jpa_repository_state_id)
  references state;

alter table state_state_actions
  add constraint fk_state_state_actions_a
  foreign key (state_actions_id)
  references action;

alter table state_state_actions
  add constraint fk_state_state_actions_s
  foreign key (jpa_repository_state_id)
  references state;

alter table transition
  add constraint fk_transition_guard
  foreign key (guard_id)
  references guard;

alter table transition
  add constraint fk_transition_source
  foreign key (source_id)
  references state;

alter table transition
  add constraint fk_transition_target
  foreign key (target_id)
  references state;

alter table transition_actions
  add constraint fk_transitin_actions_a
  foreign key (actions_id)
  references action;

alter table transition_actions
  add constraint fk_transition_actions_t
  foreign key (jpa_repository_transition_id)
  references transition;

-- ###################################
--  Spring Cloud Data Flow V2
-- ###################################
alter table stream_definitions add column description varchar(255);

alter table stream_definitions add column original_definition text;

alter table task_definitions add column description varchar(255);

CREATE TABLE task_execution_metadata (
  id int8 NOT NULL,
  task_execution_id int8 NOT NULL,
  task_execution_manifest TEXT,
  primary key (id),
  CONSTRAINT TASK_METADATA_FK FOREIGN KEY (task_execution_id)
  REFERENCES TASK_EXECUTION(TASK_EXECUTION_ID)
);

CREATE SEQUENCE task_execution_metadata_seq MAXVALUE 9223372036854775807 NO CYCLE;

-- ###################################
--  Spring Cloud Data Flow V2 After
-- ###################################
update stream_definitions set original_definition=definition;

-- ###################################
--  Spring Cloud Data Flow V3
-- ###################################
alter table audit_records add platform_name varchar(255);

-- ###################################
--  Spring Cloud Data Flow V4
-- ###################################
create index STEP_NAME_IDX on BATCH_STEP_EXECUTION (STEP_NAME);

-- ###################################
--  Spring Cloud Data Flow V5
-- ###################################
create index TASK_EXECUTION_ID_IDX on TASK_EXECUTION_PARAMS (TASK_EXECUTION_ID);

-- ###################################
--  Spring Cloud Data Flow V6
-- ###################################
alter table app_registration add boot_version varchar(16);

-- ###################################
--  Spring Cloud Data Flow V7
-- ###################################
CREATE TABLE BOOT3_TASK_EXECUTION
(
    TASK_EXECUTION_ID     BIGINT NOT NULL PRIMARY KEY,
    START_TIME            TIMESTAMP DEFAULT NULL,
    END_TIME              TIMESTAMP DEFAULT NULL,
    TASK_NAME             VARCHAR(100),
    EXIT_CODE             INTEGER,
    EXIT_MESSAGE          VARCHAR(2500),
    ERROR_MESSAGE         VARCHAR(2500),
    LAST_UPDATED          TIMESTAMP,
    EXTERNAL_EXECUTION_ID VARCHAR(255),
    PARENT_EXECUTION_ID   BIGINT
);

CREATE TABLE BOOT3_TASK_EXECUTION_PARAMS
(
    TASK_EXECUTION_ID BIGINT NOT NULL,
    TASK_PARAM        VARCHAR(2500),
    constraint BOOT3_TASK_EXEC_PARAMS_FK foreign key (TASK_EXECUTION_ID)
        references BOOT3_TASK_EXECUTION (TASK_EXECUTION_ID)
);

CREATE TABLE BOOT3_TASK_TASK_BATCH
(
    TASK_EXECUTION_ID BIGINT NOT NULL,
    JOB_EXECUTION_ID  BIGINT NOT NULL,
    constraint BOOT3_TASK_EXEC_BATCH_FK foreign key (TASK_EXECUTION_ID)
        references BOOT3_TASK_EXECUTION (TASK_EXECUTION_ID)
);

CREATE SEQUENCE BOOT3_TASK_SEQ MAXVALUE 9223372036854775807 NO CYCLE;

CREATE TABLE BOOT3_TASK_LOCK
(
    LOCK_KEY     CHAR(36)     NOT NULL,
    REGION       VARCHAR(100) NOT NULL,
    CLIENT_ID    CHAR(36),
    CREATED_DATE TIMESTAMP    NOT NULL,
    constraint BOOT3_LOCK_PK primary key (LOCK_KEY, REGION)
);

CREATE TABLE BOOT3_TASK_EXECUTION_METADATA
(
    ID                      BIGINT NOT NULL,
    TASK_EXECUTION_ID       BIGINT NOT NULL,
    TASK_EXECUTION_MANIFEST TEXT,
    primary key (ID),
    CONSTRAINT BOOT3_TASK_METADATA_FK FOREIGN KEY (TASK_EXECUTION_ID) REFERENCES BOOT3_TASK_EXECUTION (TASK_EXECUTION_ID)
);

CREATE TABLE BOOT3_BATCH_JOB_INSTANCE
(
    JOB_INSTANCE_ID BIGINT       NOT NULL PRIMARY KEY,
    VERSION         BIGINT,
    JOB_NAME        VARCHAR(100) NOT NULL,
    JOB_KEY         VARCHAR(32)  NOT NULL,
    constraint BOOT3_JOB_INST_UN unique (JOB_NAME, JOB_KEY)
);

CREATE TABLE BOOT3_BATCH_JOB_EXECUTION
(
    JOB_EXECUTION_ID BIGINT    NOT NULL PRIMARY KEY,
    VERSION          BIGINT,
    JOB_INSTANCE_ID  BIGINT    NOT NULL,
    CREATE_TIME      TIMESTAMP NOT NULL,
    START_TIME       TIMESTAMP DEFAULT NULL,
    END_TIME         TIMESTAMP DEFAULT NULL,
    STATUS           VARCHAR(10),
    EXIT_CODE        VARCHAR(2500),
    EXIT_MESSAGE     VARCHAR(2500),
    LAST_UPDATED     TIMESTAMP,
    constraint BOOT3_JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
        references BOOT3_BATCH_JOB_INSTANCE (JOB_INSTANCE_ID)
);

CREATE TABLE BOOT3_BATCH_JOB_EXECUTION_PARAMS
(
    JOB_EXECUTION_ID BIGINT       NOT NULL,
    PARAMETER_NAME   VARCHAR(100) NOT NULL,
    PARAMETER_TYPE   VARCHAR(100) NOT NULL,
    PARAMETER_VALUE  VARCHAR(2500),
    IDENTIFYING      CHAR(1)      NOT NULL,
    constraint BOOT3_JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
        references BOOT3_BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
);

CREATE TABLE BOOT3_BATCH_STEP_EXECUTION
(
    STEP_EXECUTION_ID  BIGINT       NOT NULL PRIMARY KEY,
    VERSION            BIGINT       NOT NULL,
    STEP_NAME          VARCHAR(100) NOT NULL,
    JOB_EXECUTION_ID   BIGINT       NOT NULL,
    CREATE_TIME        TIMESTAMP    NOT NULL,
    START_TIME         TIMESTAMP DEFAULT NULL,
    END_TIME           TIMESTAMP DEFAULT NULL,
    STATUS             VARCHAR(10),
    COMMIT_COUNT       BIGINT,
    READ_COUNT         BIGINT,
    FILTER_COUNT       BIGINT,
    WRITE_COUNT        BIGINT,
    READ_SKIP_COUNT    BIGINT,
    WRITE_SKIP_COUNT   BIGINT,
    PROCESS_SKIP_COUNT BIGINT,
    ROLLBACK_COUNT     BIGINT,
    EXIT_CODE          VARCHAR(2500),
    EXIT_MESSAGE       VARCHAR(2500),
    LAST_UPDATED       TIMESTAMP,
    constraint BOOT3_JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
        references BOOT3_BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
);

CREATE TABLE BOOT3_BATCH_STEP_EXECUTION_CONTEXT
(
    STEP_EXECUTION_ID  BIGINT        NOT NULL PRIMARY KEY,
    SHORT_CONTEXT      VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT TEXT,
    constraint BOOT3_STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
        references BOOT3_BATCH_STEP_EXECUTION (STEP_EXECUTION_ID)
);

CREATE TABLE BOOT3_BATCH_JOB_EXECUTION_CONTEXT
(
    JOB_EXECUTION_ID   BIGINT        NOT NULL PRIMARY KEY,
    SHORT_CONTEXT      VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT TEXT,
    constraint BOOT3_JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
        references BOOT3_BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
);

CREATE SEQUENCE BOOT3_TASK_EXECUTION_METADATA_SEQ MAXVALUE 9223372036854775807 NO CYCLE;

CREATE SEQUENCE BOOT3_BATCH_STEP_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE BOOT3_BATCH_JOB_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE BOOT3_BATCH_JOB_SEQ MAXVALUE 9223372036854775807 NO CYCLE;

-- ###################################
--  Spring Cloud Data Flow V8
-- ###################################
CREATE VIEW AGGREGATE_TASK_EXECUTION AS
SELECT TASK_EXECUTION_ID, START_TIME, END_TIME, TASK_NAME, EXIT_CODE, EXIT_MESSAGE, ERROR_MESSAGE, LAST_UPDATED, EXTERNAL_EXECUTION_ID, PARENT_EXECUTION_ID, 'boot2' AS SCHEMA_TARGET FROM TASK_EXECUTION
UNION ALL
SELECT TASK_EXECUTION_ID, START_TIME, END_TIME, TASK_NAME, EXIT_CODE, EXIT_MESSAGE, ERROR_MESSAGE, LAST_UPDATED, EXTERNAL_EXECUTION_ID, PARENT_EXECUTION_ID, 'boot3' AS SCHEMA_TARGET FROM BOOT3_TASK_EXECUTION;

CREATE VIEW AGGREGATE_TASK_EXECUTION_PARAMS AS
SELECT TASK_EXECUTION_ID, TASK_PARAM, 'boot2' AS SCHEMA_TARGET FROM TASK_EXECUTION_PARAMS
UNION ALL
SELECT TASK_EXECUTION_ID, TASK_PARAM, 'boot3' AS SCHEMA_TARGET FROM BOOT3_TASK_EXECUTION_PARAMS;

CREATE VIEW AGGREGATE_JOB_EXECUTION AS
SELECT JOB_EXECUTION_ID, VERSION, JOB_INSTANCE_ID, CREATE_TIME, START_TIME, END_TIME, STATUS, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED, 'boot2' AS SCHEMA_TARGET FROM BATCH_JOB_EXECUTION
UNION ALL
SELECT JOB_EXECUTION_ID, VERSION, JOB_INSTANCE_ID, CREATE_TIME, START_TIME, END_TIME, STATUS, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED, 'boot3' AS SCHEMA_TARGET FROM BOOT3_BATCH_JOB_EXECUTION;

CREATE VIEW AGGREGATE_JOB_INSTANCE AS
SELECT JOB_INSTANCE_ID, VERSION, JOB_NAME, JOB_KEY, 'boot2' AS SCHEMA_TARGET FROM BATCH_JOB_INSTANCE
UNION ALL
SELECT JOB_INSTANCE_ID, VERSION, JOB_NAME, JOB_KEY, 'boot3' AS SCHEMA_TARGET FROM BOOT3_BATCH_JOB_INSTANCE;

CREATE VIEW AGGREGATE_TASK_BATCH AS
SELECT TASK_EXECUTION_ID, JOB_EXECUTION_ID, 'boot2' AS SCHEMA_TARGET FROM TASK_TASK_BATCH
UNION ALL
SELECT TASK_EXECUTION_ID, JOB_EXECUTION_ID, 'boot3' AS SCHEMA_TARGET FROM BOOT3_TASK_TASK_BATCH;

CREATE VIEW AGGREGATE_STEP_EXECUTION AS
SELECT STEP_EXECUTION_ID, VERSION, STEP_NAME, JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, COMMIT_COUNT, READ_COUNT, FILTER_COUNT, WRITE_COUNT, READ_SKIP_COUNT, WRITE_SKIP_COUNT, PROCESS_SKIP_COUNT, ROLLBACK_COUNT, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED, 'boot2' AS SCHEMA_TARGET FROM BATCH_STEP_EXECUTION
UNION ALL
SELECT STEP_EXECUTION_ID, VERSION, STEP_NAME, JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, COMMIT_COUNT, READ_COUNT, FILTER_COUNT, WRITE_COUNT, READ_SKIP_COUNT, WRITE_SKIP_COUNT, PROCESS_SKIP_COUNT, ROLLBACK_COUNT, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED, 'boot3' AS SCHEMA_TARGET FROM BOOT3_BATCH_STEP_EXECUTION;

-- ###################################
--  Spring Cloud Data Flow V9
-- ###################################
ALTER TABLE task_execution_metadata RENAME TO task_execution_metadata_lc;
ALTER TABLE task_execution_metadata_lc RENAME TO TASK_EXECUTION_METADATA;
ALTER SEQUENCE task_execution_metadata_seq RENAME TO task_execution_metadata_seq_lc;
ALTER SEQUENCE task_execution_metadata_seq_lc RENAME TO TASK_EXECUTION_METADATA_SEQ;