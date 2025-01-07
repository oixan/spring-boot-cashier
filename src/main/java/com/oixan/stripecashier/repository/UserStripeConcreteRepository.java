package com.oixan.stripecashier.repository;

import org.springframework.stereotype.Repository;
import com.oixan.stripecashier.entity.UserAccount;

@Repository
public interface UserStripeConcreteRepository extends UserStripeRepository<UserAccount, Long> {
}
