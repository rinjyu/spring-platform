package com.spring.batch.common.listener;

import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;


@Component
public class BatchSkipListener implements SkipListener {

    @Override
    public void onSkipInRead(Throwable throwable) {

    }

    @Override
    public void onSkipInWrite(Object o, Throwable throwable) {

    }

    @Override
    public void onSkipInProcess(Object o, Throwable throwable) {

    }
}