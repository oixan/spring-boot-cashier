package com.oixan.stripecashier.support;

import java.util.concurrent.CountDownLatch;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder implements ApplicationContextAware, ApplicationListener<ApplicationContextEvent> {

    private static ApplicationContext context;
    private static final CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
        latch.countDown();
        System.out.println("ApplicationContext is set on setApplicationContext method");
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        context = event.getApplicationContext();
        latch.countDown();
        System.out.println("ApplicationContext is set on onApplicationEvent method");
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
