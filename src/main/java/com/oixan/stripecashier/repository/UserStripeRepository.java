package com.oixan.stripecashier.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * A generic repository interface for entities associated with a Stripe user.
 * 
 * <p>This interface extends {@link JpaRepository} to provide standard CRUD operations
 * and includes an additional method to find an entity by its Stripe ID.</p>
 *
 * @param <T> the type of the entity managed by this repository.
 * @param <ID> the type of the entity's identifier.
 */
@NoRepositoryBean
public interface UserStripeRepository<T, ID> extends JpaRepository<T, ID> {

    /**
     * Finds an entity by its Stripe ID.
     *
     * @param stripeId the Stripe ID of the entity to be found.
     * @return an {@link Optional} containing the entity if found, or empty if not found.
     */
    Optional<T> findByStripeId(String stripeId);
}
