package com.oixan.stripecashier.factory;

import com.oixan.stripecashier.builder.StripeBuilder;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;

public class PaymentMethodsManagerFactory {

	public static PaymentMethodsManager create(CustomerManager cm) {
		return new PaymentMethodsManager(
						new StripeBuilder(PropertiesFactory.create())
					)
					.setCustomerManager(cm);
	}
	
}
