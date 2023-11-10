package com.spring.batch.common.mapping;

import org.springframework.batch.extensions.excel.mapping.BeanWrapperRowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;
import org.springframework.validation.BindException;

public class ExcelBeanWrapperRowMapper<T> extends BeanWrapperRowMapper<T> {

    @Override
    public T mapRow(RowSet rs) throws BindException {
        return super.mapRow(rs);
    }
}
