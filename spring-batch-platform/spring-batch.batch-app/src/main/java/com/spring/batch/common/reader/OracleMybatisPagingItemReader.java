package com.spring.batch.common.reader;

import com.spring.batch.common.component.ApplicationContextHolder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;

public class OracleMybatisPagingItemReader<T> extends MyBatisPagingItemReaderBuilder<T> {

    @Override
    public MyBatisPagingItemReader<T> build() {
        super.sqlSessionFactory(ApplicationContextHolder.getBean("oracleSqlSessionFactory", SqlSessionFactory.class));
        return super.build();
    }
}