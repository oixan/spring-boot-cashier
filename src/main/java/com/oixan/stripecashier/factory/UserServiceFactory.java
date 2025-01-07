package com.oixan.stripecashier.factory;

import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.service.UserService;
import com.oixan.stripecashier.singleton.ApplicationContextSingleton;

public class UserServiceFactory {
	
	  private static UserService<?, ?> userService;


		@SuppressWarnings("unchecked")
	  public static <T extends IUserStripe, ID> UserService<T, ID> create(Class<T> userClass, Class<ID> idClass) {
		  if (userService != null) {
			  return (UserService<T, ID>) userService;
			}

			userService = ApplicationContextSingleton
																	.create()
																	.getBean(UserService.class);

			return (UserService<T, ID>) userService;
	 }
}
