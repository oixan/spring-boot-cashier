package com.oixan.stripecashier.builder;

import java.util.function.BiFunction;

import org.springframework.stereotype.Component;

/**
 * The    class is responsible for configuring and initializing Stripe's API settings.
 * It manages settings such as subscription behavior, tax calculations, currency, and custom formatters.
 */
@Component
public class StripeBuilder {

    /**
     * Indicates if subscriptions marked as past due will be deactivated.
     * Defaults to {@code true}.
     */
    private boolean deactivatePastDue = true;

    /**
     * Indicates if subscriptions marked as incomplete will be deactivated.
     * Defaults to {@code true}.
     */
    private boolean deactivateIncomplete = true;

    /**
     * Indicates if taxes will be automatically calculated using Stripe Tax.
     * Defaults to {@code false}.
     */
    private boolean calculatesTaxes = false;

    /**
     * The default customer model class name.
     * Can be set to define a custom customer model for integration.
     * Defaults to {@code null}.
     */
    private String customerModel = null;

    /**
     * The default currency used for Stripe transactions.
     * Defaults to {@code "usd"}.
     */
    private String currency = "usd";

    /**
     * A custom formatter for displaying currency values.
     * Accepts a {@code BiFunction} with parameters {@code Integer} for the amount and {@code String} for the currency code.
     * Can be used to override the default currency formatting.
     */
    private static BiFunction<Integer, String, String> formatCurrencyUsing;

    /**
     * Constructs a new    instance and initializes Stripe's API key using the provided {@code StripeProperties}.
     *
     */
    public StripeBuilder() {

    }

    /**
     * Sets the custom currency formatter for displaying currency values.
     *
     * @return boolean
     */
    public boolean isDeactivatePastDue() {
        return deactivatePastDue;
    }

    /**
     * Sets the custom currency formatter for displaying currency values.
     *
     * @param deactivatePastDue the custom currency formatter
     */
    public void setDeactivatePastDue(boolean deactivatePastDue) {
        this.deactivatePastDue = deactivatePastDue;
    }

    /**
     * Returns the custom currency formatter for displaying currency values.
     *
     * @return the custom currency formatter
     */
    public boolean isDeactivateIncomplete() {
        return deactivateIncomplete;
    }

    /**
     * Sets the custom currency formatter for displaying currency values.
     *
     * @param deactivateIncomplete the custom currency formatter
     */
    public void setDeactivateIncomplete(boolean deactivateIncomplete) {
        this.deactivateIncomplete = deactivateIncomplete;
    }

    /**
     * Returns the custom currency formatter for displaying currency values.
     *
     * @return the custom currency formatter
     */
    public boolean isCalculatesTaxes() {
        return calculatesTaxes;
    }

    /**
     * Sets the custom currency formatter for displaying currency values.
     * 
     * @param calculatesTaxes
     */
    public void setCalculatesTaxes(boolean calculatesTaxes) {
        this.calculatesTaxes = calculatesTaxes;
    }

    /**
     * Returns the custom currency formatter for displaying currency values.
     *
     * @return the custom currency formatter
     */
    public String getCustomerModel() {
        return customerModel;
    }

    /**
     * Sets the custom currency formatter for displaying currency values.
     *
     * @param customerModel the custom currency formatter
     */
    public void setCustomerModel(String customerModel) {
        this.customerModel = customerModel;
    }

    /**
     * Returns the custom currency formatter for displaying currency values.
     *
     * @return the custom currency formatter
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the custom currency formatter for displaying currency values.
     *
     * @param currency the custom currency formatter
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
