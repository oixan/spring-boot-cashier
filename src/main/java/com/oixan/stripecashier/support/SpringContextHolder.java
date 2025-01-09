package com.oixan.stripecashier.support;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;
    private static final CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
        latch.countDown();
    }

    public static <T> T getBean(Class<T> requiredType) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread interrupted while waiting for ApplicationContext initialization", e);
        }

        if (context == null) {
            throw new IllegalStateException("ApplicationContext is not initialized yet.");
        }
        return context.getBean(requiredType);
    }
}
