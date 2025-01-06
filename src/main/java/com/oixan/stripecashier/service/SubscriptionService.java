package com.oixan.stripecashier.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oixan.stripecashier.entity.Subscription;
import com.oixan.stripecashier.repository.SubscriptionRepository;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public Optional<Subscription> getSubscriptionByUserIdAndType(String userId, String type) {
        return subscriptionRepository.findByUserIdAndType(userId, type);
    }

    public List<Subscription> getAllSubscriptionsByUserId(String userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public void deleteSubscriptionById(Long id) {
        subscriptionRepository.deleteById(id);
    }

    public void deleteSubscriptionByStripeId(String id) {
        subscriptionRepository.deleteByStripeId(id);
    }

    public void updateSubscriptionEndsAt(Long id, String endsAt) {
        subscriptionRepository.updateEndsAt(id, endsAt);
    }
}
