package com.oixan.stripecashier.factory;

import com.oixan.stripecashier.config.StripeProperties;
import com.oixan.stripecashier.singleton.ApplicationContextSingleton;

public class PropertiesFactory {
	
	  private static StripeProperties properties;

	  public static StripeProperties create() {
		  if (properties != null) {
			  return properties;
			}

			properties = ApplicationContextSingleton
																	.create()
																	.getBean(StripeProperties.class);

			return properties;
	 }
}
