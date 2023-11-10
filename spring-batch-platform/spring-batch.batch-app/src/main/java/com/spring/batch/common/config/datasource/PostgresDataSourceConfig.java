package com.spring.batch.common.config.datasource;

import com.spring.batch.common.config.datasource.marker.PostgresMapper;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@RefreshScope
@Configuration
@MapperScan(basePackages = "com.spring.batch",
            sqlSessionFactoryRef = "postgresSqlSessionFactory",
            annotationClass = PostgresMapper.class)
@EnableTransactionManagement
@RequiredArgsConstructor
public class PostgresDataSourceConfig {

    private final ApplicationContext applicationContext;

    @Value("${spring.postgres.datasource.mybatis.config}")
    private String mybatisConfig;

    @Value("${spring.postgres.datasource.mybatis.mapper-locations}")
    private String mybatisMapperLocations;

    @Bean
    @Qualifier("postgresDataSource")
    @ConfigurationProperties(prefix = "spring.postgres.datasource")
    public DataSource postgresDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @Qualifier("postgresSqlSessionFactory")
    public SqlSessionFactory postgresSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(postgresDataSource());
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource(mybatisConfig));
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(mybatisMapperLocations));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    @Qualifier("postgresTransactionManager")
    public DataSourceTransactionManager postgresTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(postgresDataSource());
        return dataSourceTransactionManager;
    }

    @Bean
    @Qualifier("postgresSqlSessionTemplate")
    public SqlSessionTemplate postgresSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(postgresSqlSessionFactory());
    }

    @Bean
    @Qualifier("postgresSqlSessionBatchTemplate")
    public SqlSessionTemplate postgresSqlSessionBatchTemplate() throws Exception {
        return new SqlSessionTemplate(postgresSqlSessionFactory(), ExecutorType.BATCH);
    }
}
