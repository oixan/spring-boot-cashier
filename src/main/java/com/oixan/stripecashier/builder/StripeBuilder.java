package com.oixan.stripecashier.builder;

import java.util.function.BiFunction;

import org.springframework.stereotype.Component;

import com.oixan.stripecashier.config.StripeProperties;
import com.stripe.Stripe;

public class StripeBuilder {
	
    /**
     * Indicates if Cashier will mark past due subscriptions as inactive.
     */
    private static boolean deactivatePastDue = true;

    /**
     * Indicates if Cashier will mark incomplete subscriptions as inactive.
     */
    private static boolean deactivateIncomplete = true;

    /**
     * Indicates if Cashier will automatically calculate taxes using Stripe Tax.
     */
    private static boolean calculatesTaxes = false;

    /**
     * The default customer model class name.
     */
    private static String customerModel = null;

    /**
     * Indicates the currency used by Stripe
     */
	private static String currency = "usd";
	
    /**
     * Custom currency formatter.
     */
    private static BiFunction<Integer, String, String> formatCurrencyUsing;


    public StripeBuilder(StripeProperties stripeProperties) {
        Stripe.apiKey = stripeProperties.getApiKey();
    }

}
