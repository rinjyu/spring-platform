package com.spring.batch.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BatchItemReadListener implements ItemReadListener {

    @Override
    public void beforeRead() {

    }

    @Override
    public void afterRead(Object o) {

    }

    @Override
    public void onReadError(Exception e) {
        log.error(">> onReadError ::: " + e.getMessage());
    }
}
