package com.oixan.stripecashier.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface UserStripeRepository<T, ID> extends JpaRepository<T, ID> {
    Optional<T> findByStripeId(String stripeId);
}
