package com.oixan.stripecashier.manager;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oixan.stripecashier.factory.SubscriptionServiceFactory;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.service.SubscriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionItem;
import com.stripe.param.SubscriptionUpdateParams;

/**
 * Manages subscription-related operations.
 */
@Component
public class SubscriptionManager {
	
	@Autowired
	private SubscriptionServiceFactory subscriptionServiceFactory;
	
    /**
     * Manages Stripe customers.
     */
    private CustomerManager customerManager;

    /**
     * Represents the user associated with Stripe operations.
     */
    private IUserStripe user;

    /**
     * Service for managing subscriptions.
     */
    @Autowired
    private SubscriptionService subscriptionService;

    /**
     * Manages subscription-related operations.
     * This class is responsible for handling the creation and management of subscriptions
     * using the SubscriptionService.
     */
    public SubscriptionManager() {
    }

    /**
     * Sets the customer manager for the subscription.
     *
     * @param cm the customer manager
     * @return the current instance of {@code SubscriptionBuilder} for method chaining
     */
    public SubscriptionManager setCustomerManager(CustomerManager cm) {
    	customerManager = cm;
    	return this;
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
    
    /**
     * Swaps the subscription item with a new price.
     * <p>
     * This method updates the subscription by replacing the current item with a new price.
     * The update is applied immediately with proration, and the old subscription items are removed.
     * 
     * @param type The type of the subscription to be updated.
     * @param newPriceId The new price ID to replace the current item.
     * @return The updated subscription.
     * @throws StripeException If there is an error updating the subscription with Stripe.
     */
    public Subscription swapItemSubscription(String type, String newPriceId) throws StripeException {
        // Retrieve the subscription from the database based on the customer and type
        Optional<com.oixan.stripecashier.entity.Subscription> targetSubscription = getSubscriptionFromDatabase(
                customerManager.getUser().getStripeId(), type);

        if (!targetSubscription.isPresent()) {
            throw new IllegalArgumentException("No subscription found with the specified type for the user.");
        }

        // Extract subscription details from the database entity
        com.oixan.stripecashier.entity.Subscription subscriptionStripeEntity = targetSubscription.get();
        String subscriptionId = subscriptionStripeEntity.getStripeId();

        // Fetch the existing subscription from Stripe
        Subscription subscriptionStripe = Subscription.retrieve(subscriptionId);
        
        // Create the new item with the specified price and quantity
        SubscriptionUpdateParams.Item newItem = SubscriptionUpdateParams.Item.builder()
                .setPrice(newPriceId)
                .setQuantity(subscriptionStripeEntity.getQuantity())
                .build();

        // Build the update parameters with immediate proration behavior
        SubscriptionUpdateParams updateParams = SubscriptionUpdateParams.builder()
                .setProrationBehavior(SubscriptionUpdateParams.ProrationBehavior.ALWAYS_INVOICE) // Apply changes immediately
                .setCancelAtPeriodEnd(false)
                .addItem(newItem)
                .build();

  
        // Update the subscription on Stripe
        Subscription updatedSubscription;
        try {
            updatedSubscription = subscriptionStripe.update(updateParams);
        } catch (StripeException e) {
            System.err.println("Error updating subscription: " + e.getMessage());
            throw e;
        }
        
        // Remove all old subscription items except the new one
        for (SubscriptionItem item : subscriptionStripe.getItems().getData()) {
            // Skip the newly added item
            if (!item.getPrice().getId().equals(newPriceId)) {
                SubscriptionItem.retrieve(item.getId()).delete();
            }
        }
        
        // Save the updated subscription to the database
        saveUpdatedSubscription(updatedSubscription, type);

        return updatedSubscription;
    }

    
    /**
     * Retrieves a subscription entity from the database.
     * 
     * @param userId the user ID
     * @param type the subscription type
     * @return an Optional containing the subscription entity if found, otherwise an empty Optional
     */
    private Optional<com.oixan.stripecashier.entity.Subscription> getSubscriptionFromDatabase(String userId, String type) {
        return subscriptionServiceFactory.create().getSubscriptionByUserIdAndType(userId, type);
    }


    /**
     * Saves the updated subscription to the database.
     * 
     * @param subscription the updated subscription object
     * @param type the subscription type
     */
    private void saveUpdatedSubscription(Subscription subscription, String type) {
        com.oixan.stripecashier.entity.Subscription entity = new com.oixan.stripecashier.entity.Subscription();
        entity.setUserId(subscription.getCustomer());
        entity.setStripeId(subscription.getId());
        entity.setStripeStatus(subscription.getStatus());
        entity.setStripePrice(subscription.getItems().getData().get(0).getPrice().getId());
        entity.setQuantity(subscription.getItems().getData().get(0).getQuantity());
        entity.setType(type);
        
        // Set the end date using LocalDateTime
        if (subscription.getCancelAt() != null) {
            entity.setEndsAt(Instant.ofEpochSecond(subscription.getCancelAt())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        } else if (subscription.getCurrentPeriodEnd() != null) {
            entity.setEndsAt(Instant.ofEpochSecond(subscription.getCurrentPeriodEnd())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        } else {
            entity.setEndsAt(null);
        }

        subscriptionServiceFactory.create().updateSubscription(entity);
    }


    /**
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
