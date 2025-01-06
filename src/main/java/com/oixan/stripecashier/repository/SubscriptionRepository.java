package com.oixan.stripecashier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oixan.stripecashier.entity.Subscription;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    /**
     * Finds a subscription by user ID and type.
     *
     * @param userId the ID of the user.
     * @param type   the type of the subscription.
     * @return an Optional containing the subscription if found, otherwise empty.
     */
    Optional<Subscription> findByUserIdAndType(String userId, String type);

    
    /**
     * Finds all subscriptions by user ID.
     *
     * @param userId the ID of the user.
     * @return a list of subscriptions associated with the user.
     */
    List<Subscription> findByUserId(String userId);


    /**
     * Deletes a subscription by Stripe ID.
     *
     * @param stripeId the Stripe ID of the subscription.
     */
    void deleteByStripeId(String stripeId);
}