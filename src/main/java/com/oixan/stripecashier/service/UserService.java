package com.oixan.stripecashier.service;

import java.lang.reflect.Field;
import java.util.Optional;

import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.repository.UserStripeRepository;
import com.oixan.stripecashier.support.Classes;


public class UserService<T extends IUserStripe, D> {

    private final UserStripeRepository<T, D> repository;

    public UserService(UserStripeRepository<T, D> repository) {
        this.repository = repository;
    }

    public T save(T user) {
        return repository.save(user);
    }

    public T delete(T user) {
        repository.delete(user);
        return user;
    }


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