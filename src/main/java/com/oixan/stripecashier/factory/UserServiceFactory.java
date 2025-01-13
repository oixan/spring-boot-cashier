package com.oixan.stripecashier.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.service.UserServiceStripe;

/**
 * The {@code UserServiceFactory} class provides a mechanism to create and retrieve a singleton instance of
 * {@link UserService} with type parameters for user and ID types.
 * <p>	
 * This factory ensures that only one instance of {@link UserService} is created and reused throughout the
 * application. The instance is retrieved from the Spring application context.
 */
@Component
public class UserServiceFactory {

    @Autowired
    private UserServiceStripe<?, ?> userServiceStripe;

    /**
     * Private constructor to prevent instantiation of this singleton class.
     */
    private UserServiceFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates or retrieves a {@link UserService} instance with the specified type parameters for the user and ID types.
     * <p>
     * If the {@link UserService} instance has already been created, it will be returned directly. Otherwise,
     * it will be retrieved from the Spring application context using {@link ApplicationContextSingleton}.
     *
     * @param <T>       The type of user, extending {@link IUserStripe}
     * @param <ID>      The type of the user's ID
     * @param userClass The {@link Class} of the user type
     * @param idClass   The {@link Class} of the ID type
     * @return The {@link UserService} instance with the specified type parameters
     * @throws ClassCastException If the cached instance is not compatible with the requested type parameters
     */
    @SuppressWarnings("unchecked")
    public <T extends IUserStripe, ID> UserServiceStripe<T, ID> create(Class<T> userClass, Class<ID> idClass) {
        return (UserServiceStripe<T, ID>) userServiceStripe;
    }
}
