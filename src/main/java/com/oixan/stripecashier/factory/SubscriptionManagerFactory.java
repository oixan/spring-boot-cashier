package com.oixan.stripecashier.factory;

import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.manager.SubscriptionManager;

public class SubscriptionManagerFactory {

	public static SubscriptionManager create(IUserStripe user) {
		return new SubscriptionManager()
				    						.setUser(user);
	}
}
