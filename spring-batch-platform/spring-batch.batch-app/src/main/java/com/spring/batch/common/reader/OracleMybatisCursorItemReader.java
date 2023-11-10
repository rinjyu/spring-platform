package com.spring.batch.common.reader;

import com.spring.batch.common.component.ApplicationContextHolder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;

public class OracleMybatisCursorItemReader<T> extends MyBatisCursorItemReaderBuilder<T> {

    @Override
    public MyBatisCursorItemReader<T> build() {
        super.sqlSessionFactory(ApplicationContextHolder.getBean("oracleSqlSessionFactory", SqlSessionFactory.class));
        return super.build();
    }
}
