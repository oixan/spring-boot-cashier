package com.oixan.stripecashier.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.oixan.stripecashier.factory.SubscriptionServiceFactory;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;

public class SubscriptionManager {

    private IUserStripe user;

    public SubscriptionManager() {
    }


    /**
     * Sets the user for the CustomerManager.
     *
     * @param user the IUserStripe instance to be set
     * @return the current instance of CustomerManager
     */
    public SubscriptionManager setUser(IUserStripe user) {
      this.user = user;
      return this;
    }
  

    /**
     * Cancels the subscription at the end of the current billing period.
     *
     * @return The updated Stripe subscription object.
     * @throws StripeException If an error occurs while interacting with the Stripe API.
     * @throws IllegalArgumentException If the subscription type is null, empty, or the subscription is not found.
     */
    public Subscription cancelAtPeriodEnd() throws StripeException {
      return cancelAtPeriodEnd("default");
    }


    /**
     * Cancels the subscription at the end of the current billing period.
     *
     * @param type The type of subscription to be canceled.
     * @return The updated Stripe subscription object.
     * @throws StripeException If an error occurs while interacting with the Stripe API.
     * @throws IllegalArgumentException If the subscription type is null, empty, or the subscription is not found.
     */
    public Subscription cancelAtPeriodEnd(String type) throws StripeException {
      if (type == null || type.isEmpty()) {
          throw new IllegalArgumentException("Subscription type is required.");
      }

      Map<String, Object> params = new HashMap<>();
      params.put("cancel_at_period_end", true);

      Optional<com.oixan.stripecashier.entity.Subscription> subscriptionEntity = getSubscriptionEntity(user, type);

      if (subscriptionEntity.isEmpty()) {
          throw new IllegalArgumentException("Subscription not found.");
      }

      Subscription stripeSubscription = Subscription.retrieve(subscriptionEntity.get().getStripeId());
      stripeSubscription = stripeSubscription.update(params);

      return stripeSubscription;
    }


    /**
     * Retrieves a subscription entity for a given user and subscription type.
     *
     * @param user the user whose subscription entity is to be retrieved
     * @param type the type of subscription to be retrieved
     * @return an Optional containing the subscription entity if found, otherwise an empty Optional
     */
    private Optional<com.oixan.stripecashier.entity.Subscription> getSubscriptionEntity(IUserStripe user, String type) {
      return SubscriptionServiceFactory.create()
                  .getSubscriptionByUserIdAndType(user.getStripeId(), type);
    }
    
}
