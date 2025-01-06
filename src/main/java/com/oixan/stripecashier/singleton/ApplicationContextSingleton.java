package com.oixan.stripecashier.singleton;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.oixan.stripecashier.config.AppConfig;


public class ApplicationContextSingleton {
  
   private static ApplicationContext context;

   public static ApplicationContext create() {
		  if (ApplicationContextSingleton.context != null) {
		      return ApplicationContextSingleton.context;
			}

		  ApplicationContextSingleton.context = new AnnotationConfigApplicationContext(AppConfig.class);

			return ApplicationContextSingleton.context;		
	 }
}
