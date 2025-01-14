package com.oixan.stripecashier.service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oixan.stripecashier.entity.Subscription;
import com.oixan.stripecashier.repository.SubscriptionRepository;

/**
 * Service class for managing subscriptions.
 *
 * <p>This service provides methods to interact with the subscription data, 
 * including CRUD operations and specific queries related to subscriptions 
 * associated with a user.</p>
 */
@Service
public class SubscriptionService {

    /**
     * The repository used to access subscription data.
     */
	@Autowired
    private SubscriptionRepository subscriptionRepository;

    /**
     * Constructs a new {@link SubscriptionService} instance with the given repository.
     *
     */
    @Autowired
    public SubscriptionService() {
    }

    /**
     * Retrieves a subscription by user ID and subscription type.
     *
     * @param userId the ID of the user
     * @param type the type of the subscription
     * @return an {@link Optional} containing the {@link Subscription} if found, 
     *         otherwise an empty {@link Optional}
     */
    public Optional<Subscription> getSubscriptionByUserIdAndType(String userId, String type) {
        return subscriptionRepository.findByUserIdAndType(userId, type);
    }

    /**
     * Retrieves all subscriptions associated with a given user ID.
     *
     * @param userId the ID of the user
     * @return a list of {@link Subscription} objects associated with the user
     */
    public List<Subscription> getAllSubscriptionsByUserId(String userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    /**
     * Creates a new subscription.
     *
     * @param subscription the {@link Subscription} to be created
     * @return the created {@link Subscription}
     */
    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    /**
     * Updates an existing subscription.
     * 
     * @param updatedSubscription
     */
    public void updateSubscription(Subscription updatedSubscription) {
        Optional<Subscription> existingSubscription = subscriptionRepository.findByUserIdAndType(updatedSubscription.getUserId(), updatedSubscription.getType());
        
        if (!existingSubscription.isPresent()) {
            throw new IllegalArgumentException("Subscription with ID " + updatedSubscription.getUserId() + " and type: " + updatedSubscription.getType() + " does not exist.");
        }

        Subscription existingSub = existingSubscription.get();

        existingSub.setStripeStatus(updatedSubscription.getStripeStatus());
        existingSub.setStripePrice(updatedSubscription.getStripePrice());
        existingSub.setQuantity(updatedSubscription.getQuantity());
        existingSub.setEndsAt(updatedSubscription.getEndsAt());
        existingSub.setStripeId(updatedSubscription.getStripeId());

        subscriptionRepository.save(existingSub);
    }
    
    /**
     * Deletes a subscription by its ID.
     *
     * @param id the ID of the subscription to be deleted
     */
    public void deleteSubscriptionById(Long id) {
        subscriptionRepository.deleteById(id);
    }

    /**
     * Deletes a subscription by its Stripe ID.
     *
     * @param id the Stripe ID of the subscription to be deleted
     */
    public void deleteSubscriptionByStripeId(String id) {
        subscriptionRepository.deleteByStripeId(id);
    }

    /**
     * Updates the "ends at" timestamp for a subscription.
     *
     * @param id the ID of the subscription
     * @param endsAt the new "ends at" timestamp to be set
     */
    public void updateSubscriptionEndsAt(Long id, Instant endsAt) {
        subscriptionRepository.updateEndsAt(id, endsAt.atZone(ZoneId.systemDefault()).toLocalDateTime());
    }
}
