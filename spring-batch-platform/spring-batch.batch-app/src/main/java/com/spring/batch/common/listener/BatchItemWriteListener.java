package com.spring.batch.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class BatchItemWriteListener implements ItemWriteListener {

    @Override
    public void beforeWrite(List list) {

    }

    @Override
    public void afterWrite(List list) {

    }

    @Override
    public void onWriteError(Exception e, List list) {
        log.error(">> onWriteError ::: " + e.getMessage());
    }
}
