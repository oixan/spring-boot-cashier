package com.oixan.stripecashier.support;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType) {
        if (context == null) {
            throw new IllegalStateException("ApplicationContext is not initialized yet.");
        }
        return context.getBean(requiredType);
    }
}
