package com.oixan.stripecashier.service;

import java.lang.reflect.Field;
import java.util.Optional;

import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.repository.UserStripeRepository;
import com.oixan.stripecashier.support.Classes;

/**
 * A service class for managing user-related operations.
 *
 * @param <T> the type of user entity extending
 * @param <D> the type of the user ID
 */
public class UserService<T extends IUserStripe, D> {

    private final UserStripeRepository<T, D> repository;

    /**
     * Constructs a new instance with the given repository.
     *
     * @param repository the repository used to interact with the data store
     */
    public UserService(UserStripeRepository<T, D> repository) {
        this.repository = repository;
    }

    /**
     * Saves the provided user to the repository.
     *
     * @param user the user to be saved
     * @return the saved user
     */
    public T save(T user) {
        return repository.save(user);
    }

    /**
     * Deletes the provided user from the repository.
     *
     * @param user the user to be deleted
     * @return the deleted user
     */
    public T delete(T user) {
        repository.delete(user);
        return user;
    }

    /**
     * Retrieves a user from the repository based on the user ID.
     *
     * <p>This method attempts to find the user by accessing the user ID field
     * dynamically using reflection.</p>
     *
     * @param model the user model whose ID is to be used to find the user
     * @return an Optional containing the user if found, otherwise an empty Optional
     * @throws RuntimeException if there is an error accessing the user ID field
     */
    public Optional<T> getUserById(IUserStripe model){
        try { 
            Class<?> userClass = model.getClass();

            Field idField = Classes.findIdField(userClass);
            
            Object userId = idField.get(model);

            Optional<T> optionalUser = repository.findById((D) userId);

            return optionalUser;

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error accessing user ID field", e);
        }
    }

    /**
     * Updates the Stripe ID of a user in the repository.
     *
     * @param model the user model whose Stripe ID is to be updated
     * @param stripeId the new Stripe ID to set
     * @throws RuntimeException if the user is not found in the repository
     */
    public void updateStripeId(IUserStripe model, String stripeId) {
        Optional<T> optionalUser = getUserById(model);

        if (optionalUser.isPresent()) {
            T user = optionalUser.get();
            user.setStripeId(stripeId);
            repository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
