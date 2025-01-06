package com.oixan.stripecashier.manager;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.oixan.stripecashier.factory.SubscriptionServiceFactory;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.service.SubscriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;

public class SubscriptionManager {

    private IUserStripe user;

    private SubscriptionService subscriptionService;

    public SubscriptionManager() {
        this.subscriptionService = SubscriptionServiceFactory.create();
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

      Optional<com.oixan.stripecashier.entity.Subscription> subscriptionEntity = getSubscriptionEntity(user, type);

      if (subscriptionEntity.isEmpty()) {
          throw new IllegalArgumentException("Subscription not found.");
      }

      Map<String, Object> params = new HashMap<>();
      params.put("cancel_at_period_end", true);

      Subscription stripeSubscription = Subscription.retrieve(subscriptionEntity.get().getStripeId());
      stripeSubscription = stripeSubscription.update(params);

      updateSubscriptionEndsAt(subscriptionEntity.get().getId(), Instant.ofEpochSecond(stripeSubscription.getCurrentPeriodEnd()));

      return stripeSubscription;
    }


    /**
     * Determines if the subscription is no longer active.
     * The type of subscription to be checked with default value.
     * 
     * @return true if the subscription is canceled, false otherwise.
     * @throws IllegalArgumentException If the subscription type is null, empty, or the subscription is not found.
     */
    public boolean isCanceled() {
      return isCanceled("default");
    }


    /**
     * Determines if the subscription is no longer active.
     *
     * @param type The type of subscription to be checked.
     * @return true if the subscription is canceled, false otherwise.
     * @throws IllegalArgumentException If the subscription type is null, empty, or the subscription is not found.
     */
    public boolean isCanceled(String type) {
      if (type == null || type.isEmpty()) {
        throw new IllegalArgumentException("Subscription type is required.");
      }

      Optional<com.oixan.stripecashier.entity.Subscription> subscriptionEntity = getSubscriptionEntity(user, type);

      if (subscriptionEntity.isEmpty()) {
        throw new IllegalArgumentException("Subscription not found.");
      }

      return subscriptionEntity.get().getEndsAt() != null;
    }


    /**
     * Determines if the subscription has ended and the grace period has expired.
     * The type of subscription to be checked with default value.
     *
     * @return true if the subscription has ended and the grace period has expired, false otherwise.
     * @throws IllegalArgumentException If the subscription type is null, empty, or the subscription is not found.
     */
    public boolean ended() {
      return ended("default");
    }


    /**
     * Determines if the subscription has ended and the grace period has expired.
     *
     * @param type The type of subscription to be checked.
     * @return true if the subscription has ended and the grace period has expired, false otherwise.
     * @throws IllegalArgumentException If the subscription type is null, empty, or the subscription is not found.
     */
    public boolean ended(String type) {
      return isCanceled(type) && !onGracePeriod(type);
    }


    /**
     * Determines if the subscription is within its trial period.
     * The type of subscription to be checked with default value.
     *
     * @return true if the subscription is within its trial period, false otherwise.
     * @throws IllegalArgumentException If the subscription type is null, empty, or the subscription is not found.
     */
    public boolean onTrial() {
      return onTrial("default");
    }


    /**
     * Determines if the subscription is within its trial period.
     *
     * @param type The type of subscription to be checked.
     * @return true if the subscription is within its trial period, false otherwise.
     * @throws IllegalArgumentException If the subscription type is null, empty, or the subscription is not found.
     */
    public boolean onTrial(String type) {
      if (type == null || type.isEmpty()) {
        throw new IllegalArgumentException("Subscription type is required.");
      }

      Optional<com.oixan.stripecashier.entity.Subscription> subscriptionEntity = getSubscriptionEntity(user, type);

      if (subscriptionEntity.isEmpty()) {
        throw new IllegalArgumentException("Subscription not found.");
      }

      com.oixan.stripecashier.entity.Subscription subscription = subscriptionEntity.get();
      return subscription.getTrialEndsAt() != null && subscription.getTrialEndsAt().isAfter(java.time.LocalDateTime.now());
    }


    /**
     * Determines if the subscription is within its grace period after cancellation.
     * The type of subscription to be checked with default value.
     * 
     * @return true if the subscription is within its grace period, false otherwise.
     * @throws IllegalArgumentException If the subscription type is null, empty, or the subscription is not found.
     */
    public boolean onGracePeriod() {
      return onGracePeriod("default");
    }


    /**
     * Determines if the subscription is within its grace period after cancellation.
     *
     * @param type The type of subscription to be checked.
     * @return true if the subscription is within its grace period, false otherwise.
     * @throws IllegalArgumentException If the subscription type is null, empty, or the subscription is not found.
     */
    public boolean onGracePeriod(String type) {
      if (type == null || type.isEmpty()) {
        throw new IllegalArgumentException("Subscription type is required.");
      }

      Optional<com.oixan.stripecashier.entity.Subscription> subscriptionEntity = getSubscriptionEntity(user, type);

      if (subscriptionEntity.isEmpty()) {
        throw new IllegalArgumentException("Subscription not found.");
      }

      com.oixan.stripecashier.entity.Subscription subscription = subscriptionEntity.get();
      return subscription.getEndsAt() != null && subscription.getEndsAt().isAfter(java.time.LocalDateTime.now());
    }


    /*
     * Updates the ends_at field of a subscription entity.
     * 
     * @param id the ID of the subscription entity to be updated
     * @param endsAt the new value for the ends_at field
     * @throws IllegalArgumentException If the ID is null or empty
     */
    private void updateSubscriptionEndsAt(Long id, Instant endsAt) {
      subscriptionService.updateSubscriptionEndsAt(id, endsAt);
    }


    /**
     * Retrieves a subscription entity for a given user and subscription type.
     *
     * @param user the user whose subscription entity is to be retrieved
     * @param type the type of subscription to be retrieved
     * @return an Optional containing the subscription entity if found, otherwise an empty Optional
     */
    private Optional<com.oixan.stripecashier.entity.Subscription> getSubscriptionEntity(IUserStripe user, String type) {
      return subscriptionService.getSubscriptionByUserIdAndType(user.getStripeId(), type);
    }
    
}
