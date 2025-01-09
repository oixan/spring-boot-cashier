package com.oixan.stripecashier.builder;

import java.util.function.BiFunction;

import com.oixan.stripecashier.config.StripeProperties;
import com.stripe.Stripe;

/**
 * The {@code StripeBuilder} class is responsible for configuring and initializing Stripe's API settings.
 * It manages settings such as subscription behavior, tax calculations, currency, and custom formatters.
 */
public class StripeBuilder {

    /**
     * Indicates if subscriptions marked as past due will be deactivated.
     * Defaults to {@code true}.
     */
    private static boolean deactivatePastDue = true;

    /**
     * Indicates if subscriptions marked as incomplete will be deactivated.
     * Defaults to {@code true}.
     */
    private static boolean deactivateIncomplete = true;

    /**
     * Indicates if taxes will be automatically calculated using Stripe Tax.
     * Defaults to {@code false}.
     */
    private static boolean calculatesTaxes = false;

    /**
     * The default customer model class name.
     * Can be set to define a custom customer model for integration.
     * Defaults to {@code null}.
     */
    private static String customerModel = null;

    /**
     * The default currency used for Stripe transactions.
     * Defaults to {@code "usd"}.
     */
    private static String currency = "usd";

    /**
     * A custom formatter for displaying currency values.
     * Accepts a {@code BiFunction} with parameters {@code Integer} for the amount and {@code String} for the currency code.
     * Can be used to override the default currency formatting.
     */
    private static BiFunction<Integer, String, String> formatCurrencyUsing;

    /**
     * Constructs a new {@code StripeBuilder} instance and initializes Stripe's API key using the provided {@code StripeProperties}.
     *
     * @param stripeProperties the {@code StripeProperties} object containing Stripe configuration settings,
     *                         including the API key
     */
    public StripeBuilder(StripeProperties stripeProperties) {
        if (stripeProperties.getApiKey() == null || stripeProperties.getApiKey().isEmpty()) {
            throw new IllegalStateException("Stripe API key is not configured. Please set stripe.apiKey in your application.properties.");
        }

        Stripe.apiKey = stripeProperties.getApiKey();
    }
}
