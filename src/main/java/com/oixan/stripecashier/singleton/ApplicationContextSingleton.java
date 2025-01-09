package com.oixan.stripecashier.singleton;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.oixan.stripecashier.config.AppConfig;


/**
 * Singleton class
 *
 * <p>This class provides a single point of access to the 
 * application context. It ensures that only one instance of the context is created 
 * and used throughout the application.</p>
 */
public class ApplicationContextSingleton {
  
	/**
	 * The singleton instance of the {@link ApplicationContext}.
	 */
	private static ApplicationContext context;

	/**
	 * Creates and returns the singleton instance of the {@link ApplicationContext}.
	 *
	 * <p>If the context has already been created, the existing instance is returned.</p>
	 * <p>If the context is null, a new instance is created using the {@link AnnotationConfigApplicationContext} 
	 * with the configuration class {@link AppConfig}.</p>
	 *
	 * @return the singleton instance of the {@link ApplicationContext}
	 */
	public static ApplicationContext create() {
			if (ApplicationContextSingleton.context != null) {
					return ApplicationContextSingleton.context;
			}

			ApplicationContextSingleton.context = new AnnotationConfigApplicationContext(AppConfig.class);

			return ApplicationContextSingleton.context;     
	}
	
	/**
	 * Private constructor to prevent instantiation of this singleton class.
	 * This class should only be used through the {@link #create()} method.
	 */
	private ApplicationContextSingleton() {
		// Private constructor to prevent instantiation
	}
}
