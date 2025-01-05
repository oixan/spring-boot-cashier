package com.oixan.stripecashier.factory;

import com.oixan.stripecashier.builder.SubscriptionBuilder;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.manager.CustomerManager;

public class SubscriptionBuilderFactory {

  public static SubscriptionBuilder create(IUserStripe user)
	{
    CustomerManager cm = CustomerManagerFactory.create(user);
		return new SubscriptionBuilder(
                cm,
                PaymentMethodsManagerFactory.create(cm)
              );	
	}

}
