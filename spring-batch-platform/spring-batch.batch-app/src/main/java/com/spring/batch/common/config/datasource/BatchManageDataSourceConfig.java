package com.spring.batch.common.config.datasource;

import com.spring.batch.common.config.datasource.marker.BatchMapper;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@RefreshScope
@Configuration
@MapperScan(basePackages = "com.spring.batch",
            sqlSessionFactoryRef = "batchManageSqlSessionFactory",
            annotationClass = BatchMapper.class)
@EnableTransactionManagement
@RequiredArgsConstructor
public class BatchManageDataSourceConfig {

    private final ApplicationContext applicationContext;

    @Value("${spring.batch-manage.datasource.mybatis.config}")
    private String mybatisConfig;

    @Value("${spring.batch-manage.datasource.mybatis.mapper-locations}")
    private String mybatisMapperLocations;

    @Primary
    @Bean(name = "batchManageDataSource")
    @ConfigurationProperties(prefix = "spring.batch-manage.datasource")
    public DataSource batchManageDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = "batchManageSqlSessionFactory")
    public SqlSessionFactory batchManageSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(batchManageDataSource());
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource(mybatisConfig));
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(mybatisMapperLocations));
        return sqlSessionFactoryBean.getObject();
    }

    @Primary
    @Bean(name = "batchManageTransactionManager")
    public DataSourceTransactionManager batchManageTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(batchManageDataSource());
        return dataSourceTransactionManager;
    }

    @Primary
    @Bean(name = "batchManageSqlSessionTemplate")
    public SqlSessionTemplate batchManageSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(batchManageSqlSessionFactory());
    }
}