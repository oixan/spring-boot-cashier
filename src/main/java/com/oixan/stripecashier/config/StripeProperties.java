package com.oixan.stripecashier.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeProperties {

    @Value("${stripe.apiKey}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

}
