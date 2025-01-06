package com.oixan.stripecashier.interfaces;

import com.oixan.stripecashier.builder.CheckoutBuilder;
import com.oixan.stripecashier.builder.SubscriptionBuilder;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;
import com.oixan.stripecashier.manager.SubscriptionManager;

public interface IUserStripeAction {

	CheckoutBuilder checkout();

	SubscriptionBuilder subscribe();

	IUserStripe getUserStripe();

	SubscriptionManager subscription();

	CustomerManager customer();

	PaymentMethodsManager paymentMethod();
}
