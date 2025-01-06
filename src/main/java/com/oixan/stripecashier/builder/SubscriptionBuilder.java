package com.oixan.stripecashier.builder;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
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

    /*
     * The customer manager.
     */
    private final CustomerManager customerManager;


    /*
     * The payment methods manager.
     */
    private final PaymentMethodsManager paymentMethodsManager;


    /*
     * The price ID for the subscription.
     */
    private String priceId;


    /**
     * The trial end date for the subscription.
     */
    private Instant trialExpires;

    
    /**
     * Creates a new instance of the SubscriptionBuilder.
     *
     * @param customerManager The customer manager
     * @param paymentMethodsManager The payment methods manager
     */
    public SubscriptionBuilder(
      CustomerManager customerManager,
      PaymentMethodsManager paymentMethodsManager
    ) {
      this.customerManager = customerManager;
      this.paymentMethodsManager = paymentMethodsManager;
    }


    /**
     * Sets the trial end date for the subscription.
     *
     * @param trialExpires The trial end date
     * @return The current instance of the SubscriptionBuilder
     */
    public SubscriptionBuilder setPriceId(String priceId) {
        this.priceId = priceId;
        return this;
    }


    /**
     * Sets the trial end date for the subscription.
     * 
     * @param days
     * @return The current instance of the SubscriptionBuilder
     */
    public SubscriptionBuilder setTrialDay(int days) {
      if (days < 1) {
          throw new IllegalArgumentException("Trial days must be greater than 0.");
      }

      this.trialExpires = Instant.now().plusSeconds(days * 24 * 60 * 60);
      return this;
    }


    /**
     * Sets the trial end date for the subscription.
     * 
     * @param trialExpires
     * @return The current instance of the SubscriptionBuilder
     */
    public SubscriptionBuilder setTrialExpireDate(Instant trialExpires) {
        if (trialExpires.isBefore(Instant.now())) {
            throw new IllegalArgumentException("Trial expiration date must be in the future.");
        }

        this.trialExpires = trialExpires;
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
     * Creates a subscription to an existing product on Stripe.
     *
     * @return The created subscription
     * @throws StripeException If an error occurs during creation
     */
    public Subscription start() throws StripeException { 
      return start(null, null, null);
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

      if (subscriptionOptions == null || subscriptionOptions.isEmpty()) {
        subscriptionOptions = new HashMap<>();
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

      try {
        Subscription stripeSubscription = Subscription.create(params);

        saveSubscription(stripeSubscription, type);
                
        return stripeSubscription;
      } catch (Exception e) {
          throw new RuntimeException("Failed to create subscription on Stripe", e);
      }
  }
    

  /**
  * Adds additional options to the subscription.
  * 
  * @param options The options to be added
  * @return The updated options
  */
  private Map<String, Object> addAdditionalOptions(Map<String, Object> options) {
    Map<String, Object> updatedOptions = options;

    if (updatedOptions == null) {
      updatedOptions = new HashMap<>();
    }

    updatedOptions.put("expand", List.of("latest_invoice.payment_intent"));
    updatedOptions.put("promotion_code", null);
    updatedOptions.put("trial_end", this.getTrialEndForSubscription());
    
    return updatedOptions;
  }


  /**
   * Gets the trial end date for the subscription.
   * 
   * @return The trial end date
   */
  private String getTrialEndForSubscription() {
    if (this.trialExpires != null) {
          return String.valueOf(this.trialExpires.getEpochSecond());
    }
    return null;
  }


  /**
   * Saves the subscription to the database.
   * 
   * @param stripeSubscription The Stripe subscription
   * @param type The type of subscription
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

    SubscriptionServiceFactory.create()
                        .createSubscription(subscription);
  }

}
