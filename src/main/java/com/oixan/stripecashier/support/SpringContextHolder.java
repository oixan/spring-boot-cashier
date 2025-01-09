package com.oixan.stripecashier.support;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;
    private static final CompletableFuture<Void> contextInitialized = new CompletableFuture<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
        contextInitialized.complete(null);  // Il contesto è stato inizializzato
    }

    public static <T> T getBean(Class<T> requiredType) {
        try {
            contextInitialized.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread interrupted while waiting for ApplicationContext initialization", e);
        }

        if (context == null) {
            throw new IllegalStateException("ApplicationContext is not initialized yet.");
        }
        return context.getBean(requiredType);
    }
}