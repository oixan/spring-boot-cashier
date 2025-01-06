package com.oixan.stripecashier.manager;

import java.util.HashMap;
import java.util.Map;

import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;

public class SubscriptionManager {
  
    public Subscription cancelAtPeriodEnd(String subscriptionId) throws StripeException {
      if (subscriptionId == null || subscriptionId.isEmpty()) {
          throw new IllegalArgumentException("Subscription ID is required.");
      }

      Map<String, Object> params = new HashMap<>();
      params.put("cancel_at_period_end", true);

      Subscription stripeSubscription = Subscription.retrieve(subscriptionId);
      stripeSubscription = stripeSubscription.update(params);

      return stripeSubscription;
    }
    
}
