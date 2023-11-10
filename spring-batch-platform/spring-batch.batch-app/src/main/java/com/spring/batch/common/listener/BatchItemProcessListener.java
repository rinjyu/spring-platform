package com.spring.batch.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BatchItemProcessListener implements ItemProcessListener {

    @Override
    public void beforeProcess(Object o) {

    }

    @Override
    public void afterProcess(Object o, Object o2) {

    }

    @Override
    public void onProcessError(Object o, Exception e) {
        log.error(">> onProcessError ::: " + e.getMessage());
    }
}
