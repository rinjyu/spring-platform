package com.spring.batch.common.mapping;


import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.validation.BindException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BeanWrapperRowMapper<T> extends BeanWrapperFieldSetMapper<T> implements RowMapper<T> {

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        final FieldSet fieldSet = getFieldSet(rs);
        try {
            return super.mapFieldSet(fieldSet);
        } catch (BindException e) {
            throw new IllegalArgumentException("FieldSet에 대해 Bean을 바인딩할 수 없음");
        }
    }

    private FieldSet getFieldSet(final ResultSet rs) throws SQLException {
        final ResultSetMetaData resultSetMetaData = rs.getMetaData();
        final int columnCount = resultSetMetaData.getColumnCount();

        final List<String> tokens = new ArrayList<>();
        final List<String> names = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            tokens.add(rs.getString(i));
            names.add(resultSetMetaData.getColumnName(i));
        }

        return new DefaultFieldSet(tokens.toArray(new String[0]), names.toArray(new String[0]));
    }
}
