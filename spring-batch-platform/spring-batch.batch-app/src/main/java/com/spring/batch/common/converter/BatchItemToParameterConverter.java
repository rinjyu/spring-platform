package com.spring.batch.common.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

public class BatchItemToParameterConverter {

    public static Map<String, Object> mapConvert(Object o) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(o, Map.class);
    }

    public static Supplier<Map<String, Object>> mapSupplier(Object o) {
        return () -> Collections.unmodifiableMap(mapConvert(o));
    }

    public static <T> Converter<T, Map<String, Object>> itemToParameterMapConverter(Map map) {
        return item -> map;
    }
}
