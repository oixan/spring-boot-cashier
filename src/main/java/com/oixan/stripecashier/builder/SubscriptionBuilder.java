package com.oixan.stripecashier.builder;

import java.util.Map;
import java.util.stream.Collectors;

import com.oixan.stripecashier.factory.SubscriptionServiceFactory;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Subscription;
import com.stripe.param.SubscriptionCreateParams.Item;
import com.stripe.param.SubscriptionCreateParams;

public class SubscriptionBuilder {
	
    private final CustomerManager customerManager;
    private final PaymentMethodsManager paymentMethodsManager;

    private String priceId;

    
    public SubscriptionBuilder(
      CustomerManager customerManager,
      PaymentMethodsManager paymentMethodsManager
    ) {
      this.customerManager = customerManager;
      this.paymentMethodsManager = paymentMethodsManager;
    }

    public SubscriptionBuilder setPriceId(String priceId) {
        this.priceId = priceId;
        return this;
    }


    /**
     * Creates a subscription to an existing product on Stripe.
     *
     * @param customerOptions    The customer options for the subscription
     * @param subscriptionOptions The options for the subscription, including product and price
     * @param paymentMethod      The payment method (optional)
     * @return The created subscription
     * @throws StripeException If an error occurs during creation
     */
    public Subscription startAndCreateStripeUserAndPaymentMethod(Map<String, Object> customerOptions, Map<String, Object> subscriptionOptions, String paymentMethod) throws StripeException {
      customerManager.createOrGetStripeCustomer(customerOptions);

      if (paymentMethod == null || paymentMethod.isEmpty()) {
        throw new IllegalArgumentException("Payment method is required.");
      }

      PaymentMethod pm = paymentMethodsManager.addPaymentMethod(paymentMethod);

      return start(subscriptionOptions, pm.getId());
    }
	

    /**
     * Creates a subscription to an existing product on Stripe.
     *
     * @param paymentMethod      The payment method (optional)
     * @param customerOptions    The customer options for the subscription
     * @param subscriptionOptions The options for the subscription, including product and price
     * @return The created subscription
     * @throws StripeException If an error occurs during creation
     */
    public Subscription start(Map<String, Object> subscriptionOptions, String paymentMethod) throws StripeException {
      Customer stripeCustomer = customerManager.asStripeCustomer();

      if (stripeCustomer == null) {
          throw new IllegalArgumentException("Customer is required.");
      }

      if (priceId == null) {
          throw new IllegalArgumentException("Price ID is required.");
      }

      if (subscriptionOptions == null || subscriptionOptions.isEmpty()) {
        subscriptionOptions = Map.of();
      }

      String paymentMethodId = paymentMethod;
      if (paymentMethod == null || paymentMethod.isEmpty()) {
          paymentMethodId = paymentMethodsManager.defaultPaymentMethod().getId();
          if (paymentMethodId == null) {
              throw new IllegalArgumentException("Payment method is required.");
          }
      }

      Item item = SubscriptionCreateParams.Item.builder()
              .setPrice(priceId)  
              .setQuantity(1L)
              .build();

      SubscriptionCreateParams params = SubscriptionCreateParams.builder()
              .setCustomer(stripeCustomer.getId())
              .setDefaultPaymentMethod(paymentMethodId) 
              .addItem(item)  
              .putAllMetadata(subscriptionOptions.entrySet().stream()
                      .collect(Collectors.toMap(
                              Map.Entry::getKey,
                              e -> String.valueOf(e.getValue())
                      )))
              .build();

      try {
        Subscription stripeSubscription = Subscription.create(params);

        com.oixan.stripecashier.entity.Subscription subscription = new com.oixan.stripecashier.entity.Subscription();
        subscription.setUserId(customerManager.stripeId());
        subscription.setType("basic");
        subscription.setStripeId(stripeSubscription.getId());
        subscription.setStripeStatus(stripeSubscription.getStatus());
        SubscriptionServiceFactory.create()
                          	.createSubscription(subscription);
                
        return stripeSubscription;
      } catch (Exception e) {
          throw new RuntimeException("Failed to create subscription on Stripe", e);
      }
  }

}
