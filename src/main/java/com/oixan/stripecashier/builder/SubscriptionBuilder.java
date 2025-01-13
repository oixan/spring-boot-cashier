package com.oixan.stripecashier.builder;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oixan.stripecashier.factory.SubscriptionServiceFactory;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Subscription;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.SubscriptionCreateParams.Item;

/**
 * The {@code SubscriptionBuilder} class provides functionality to create and manage subscriptions on Stripe.
 * It supports setting custom trial periods, associating customers, handling payment methods, and defining subscription options.
 */
@Component
public class SubscriptionBuilder {
	
	@Autowired
	private SubscriptionServiceFactory subscriptionServiceFactory;

    /**
     * Manages Stripe customers.
     */
    private CustomerManager customerManager;

    /**
     * Manages Stripe payment methods.
     */
    private PaymentMethodsManager paymentMethodsManager;

    /**
     * The price ID for the subscription product.
     */
    private String priceId;

    /**
     * The trial end date for the subscription.
     */
    private Instant trialExpires;

    /**
     * Constructs a new instance with the specified managers.
     */
    public SubscriptionBuilder() {

    }
    
    
    /**
     * Sets the customer manager for the subscription.
     *
     * @param cm the customer manager
     * @return the current instance of {@code SubscriptionBuilder} for method chaining
     */
    public SubscriptionBuilder setCustomerManager(CustomerManager cm) {
    	customerManager = cm;
    	return this;
    }
    
    /**
     * Sets the payment methods manager for the subscription.
     *
     * @param pm the payment methods manager
     * @return the current instance of {@code SubscriptionBuilder} for method chaining
     */
    public SubscriptionBuilder setPaymentMethodsManager(PaymentMethodsManager pm) {
    	paymentMethodsManager = pm;
    	return this;
    }

    /**
     * Sets the price ID for the subscription.
     *
     * @param priceId the price ID of the product
     * @return the current instance of {@code SubscriptionBuilder} for method chaining
     */
    public SubscriptionBuilder setPriceId(String priceId) {
        this.priceId = priceId;
        return this;
    }

    /**
     * Sets the trial period for the subscription in days.
     *
     * @param days the number of trial days
     * @return the current instance of {@code SubscriptionBuilder} for method chaining
     * @throws IllegalArgumentException if the number of days is less than 1
     */
    public SubscriptionBuilder setTrialDay(int days) {
        if (days < 1) {
            throw new IllegalArgumentException("Trial days must be greater than 0.");
        }
        this.trialExpires = Instant.now().plusSeconds(days * 24L * 60 * 60);
        return this;
    }

    /**
     * Sets the trial end date for the subscription.
     *
     * @param trialExpires the trial expiration date
     * @return the current instance of {@code SubscriptionBuilder} for method chaining
     * @throws IllegalArgumentException if the provided date is in the past
     */
    public SubscriptionBuilder setTrialExpireDate(Instant trialExpires) {
        if (trialExpires.isBefore(Instant.now())) {
            throw new IllegalArgumentException("Trial expiration date must be in the future.");
        }
        this.trialExpires = trialExpires;
        return this;
    }

    /**
     * Creates a subscription for a customer on Stripe, associating a payment method and customer details.
     *
     * @param customerOptions the options for creating or retrieving the Stripe customer
     * @param subscriptionOptions the options for the subscription
     * @param paymentMethod the payment method to use
     * @param type the type of subscription
     * @return the created {@code Subscription}
     * @throws StripeException if an error occurs during subscription creation
     */
    public Subscription startAndCreateStripeUserAndPaymentMethod(
            Map<String, Object> customerOptions,
            Map<String, Object> subscriptionOptions,
            String paymentMethod,
            String type
    ) throws StripeException {
        customerManager.createOrGetStripeCustomer(customerOptions);

        if (paymentMethod == null || paymentMethod.isEmpty()) {
            throw new IllegalArgumentException("Payment method is required.");
        }

        PaymentMethod pm = paymentMethodsManager.addPaymentMethod(paymentMethod);

        return start(subscriptionOptions, pm.getId(), type);
    }

    /**
     * Creates a basic subscription without specifying additional options or payment methods.
     *
     * @return the created {@code Subscription}
     * @throws StripeException if an error occurs during subscription creation
     */
    public Subscription start() throws StripeException {
        return start(null, null, null);
    }

    /**
     * Creates a subscription with specified options, payment method, and type.
     *
     * @param subscriptionOptions the subscription options
     * @param paymentMethod the payment method ID
     * @param type the type of subscription
     * @return the created {@code Subscription}
     * @throws StripeException if an error occurs during subscription creation
     */
    public Subscription start(
            Map<String, Object> subscriptionOptions,
            String paymentMethod,
            String type
    ) throws StripeException {
        Customer stripeCustomer = customerManager.asStripeCustomer();

        if (stripeCustomer == null) {
            throw new IllegalArgumentException("Customer is required.");
        }

        if (type == null || type.isEmpty()) {
            type = "default";
        }

        if (priceId == null) {
            throw new IllegalArgumentException("Price ID is required.");
        }

        if (subscriptionOptions == null) {
            subscriptionOptions = new HashMap<>();
        }

        String paymentMethodId = paymentMethod != null && !paymentMethod.isEmpty()
                ? paymentMethod
                : (paymentMethodsManager.defaultPaymentMethod() != null  ? paymentMethodsManager.defaultPaymentMethod().getId() : null );

        if (paymentMethodId == null) {
            throw new IllegalArgumentException("Payment method is required.");
        }

        Item item = SubscriptionCreateParams.Item.builder()
                .setPrice(priceId)
                .setQuantity(1L)
                .build();

        Map<String, Object> updatedSubscriptionOptions = addAdditionalOptions(subscriptionOptions);

        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                .setCustomer(stripeCustomer.getId())
                .setDefaultPaymentMethod(paymentMethodId)
                .addItem(item)
                .putAllMetadata(updatedSubscriptionOptions.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> String.valueOf(e.getValue())
                        )))
                .build();

        Subscription stripeSubscription = Subscription.create(params);

        saveSubscription(stripeSubscription, type);

        return stripeSubscription;
    }

    /**
     * Adds additional metadata and options for the subscription.
     *
     * @param options the existing subscription options
     * @return the updated options map
     */
    private Map<String, Object> addAdditionalOptions(Map<String, Object> options) {
        Map<String, Object> updatedOptions = options != null ? options : new HashMap<>();
        updatedOptions.put("expand", List.of("latest_invoice.payment_intent"));
        updatedOptions.put("promotion_code", null);
        updatedOptions.put("trial_end", this.getTrialEndForSubscription());
        return updatedOptions;
    }

    /**
     * Retrieves the trial end date as a string in epoch seconds.
     *
     * @return the trial end date as a string, or {@code null} if no trial is set
     */
    private String getTrialEndForSubscription() {
        return this.trialExpires != null ? String.valueOf(this.trialExpires.getEpochSecond()) : null;
    }

    /**
     * Saves the subscription to the database.
     *
     * @param stripeSubscription the Stripe subscription object
     * @param type the type of the subscription
     */
    private void saveSubscription(Subscription stripeSubscription, String type) {
        com.oixan.stripecashier.entity.Subscription subscription = new com.oixan.stripecashier.entity.Subscription();

        subscription.setUserId(customerManager.stripeId());
        subscription.setType(type);
        subscription.setStripeId(stripeSubscription.getId());
        subscription.setStripeStatus(stripeSubscription.getStatus());
        subscription.setStripePrice(stripeSubscription.getItems().getData().get(0).getPrice().getId());
        subscription.setQuantity(stripeSubscription.getItems().getData().get(0).getQuantity());
        subscription.setTrialEndsAt(null);
        subscription.setEndsAt(null);

        subscriptionServiceFactory.create().createSubscription(subscription);
    }
}
