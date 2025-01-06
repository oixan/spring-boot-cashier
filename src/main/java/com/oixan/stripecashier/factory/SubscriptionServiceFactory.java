package com.oixan.stripecashier.factory;

import com.oixan.stripecashier.service.SubscriptionService;
import com.oixan.stripecashier.singleton.ApplicationContextSingleton;

public class SubscriptionServiceFactory {
	
	  private static SubscriptionService subscriptionService;
  
	  public static SubscriptionService create() {
		  if (subscriptionService != null) {
		      return subscriptionService;
			}

	    subscriptionService = ApplicationContextSingleton
																	.create()
																	.getBean(SubscriptionService.class);

			return subscriptionService;		
	 }
}
