package com.oixan.stripecashier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.oixan.stripecashier.entity.Subscription;

import java.time.LocalDateTime;
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


    /**
     * Updates the ends_at field of a subscription.
     *
     * @param id     the ID of the subscription.
     * @param endsAt the new value for the ends_at field.
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE subscriptions SET ends_at = :endsAt WHERE id = :id", nativeQuery = true)
    void updateEndsAt(@Param("id") Long id, @Param("endsAt") LocalDateTime endsAt);

}