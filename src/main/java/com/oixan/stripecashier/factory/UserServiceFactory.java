package com.oixan.stripecashier.factory;

import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.service.UserService;
import com.oixan.stripecashier.singleton.ApplicationContextSingleton;

/**
 * The {@code UserServiceFactory} class provides a mechanism to create and retrieve a singleton instance of
 * {@link UserService} with type parameters for user and ID types.
 * <p>
 * This factory ensures that only one instance of {@link UserService} is created and reused throughout the
 * application. The instance is retrieved from the Spring application context.
 */
public class UserServiceFactory {

    /** The cached instance of the {@link UserService}. */
    private static UserService<?, ?> userService;

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
    public static <T extends IUserStripe, ID> UserService<T, ID> create(Class<T> userClass, Class<ID> idClass) {
        // Return the existing UserService if already created
        if (userService != null) {
            return (UserService<T, ID>) userService;
        }

        // Create and cache the UserService instance from the Spring application context
        userService = ApplicationContextSingleton
                .create()
                .getBean(UserService.class);

        return (UserService<T, ID>) userService;
    }
}
