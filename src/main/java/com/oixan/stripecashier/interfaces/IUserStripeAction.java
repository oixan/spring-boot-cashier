package com.oixan.stripecashier.interfaces;

import com.oixan.stripecashier.builder.CheckoutBuilder;
import com.oixan.stripecashier.builder.SubscriptionBuilder;

public interface IUserStripeAction {

	CheckoutBuilder checkout();

	SubscriptionBuilder subscribe();

	IUserStripe getUserStripe();
}
