package com.oixan.stripecashier.interfaces;

import com.oixan.stripecashier.builder.CheckoutBuilder;
import com.oixan.stripecashier.builder.SubscriptionBuilder;
import com.oixan.stripecashier.manager.SubscriptionManager;

public interface IUserStripeAction {

	CheckoutBuilder checkout();

	SubscriptionBuilder subscribe();

	IUserStripe getUserStripe();

	SubscriptionManager subscription();
}
