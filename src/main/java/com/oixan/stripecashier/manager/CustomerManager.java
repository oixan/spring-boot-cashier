package com.oixan.stripecashier.manager;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oixan.stripecashier.builder.StripeBuilder;
import com.oixan.stripecashier.factory.UserServiceFactory;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.service.UserServiceStripe;
import com.oixan.stripecashier.support.Classes;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

/**
 * Manages Stripe customer operations for a user.
 * Provides methods for retrieving, creating, and managing Stripe customer data.
 */
@Component
public class CustomerManager {

    IUserStripe user;

    Class<?> userClass;

    Class<?> userId;

    @Autowired
    StripeBuilder stripeBuilder;
    
    @Autowired
    UserServiceFactory userServiceFactory;

    /**
     * Constructor for CustomerManager.
     *
     * @param stripeBuilder an instance of {@link StripeBuilder} used for Stripe operations
     */
    public CustomerManager() {
    }

    /**
     * Retrieve the Stripe customer ID.
     *
     * @return String|null the Stripe customer ID or null if not set
     */
    public String stripeId() {
        return this.user.getStripeId();
    }

    /**
     * Determine if the customer has a Stripe customer ID.
     *
     * @return boolean true if the customer has a Stripe ID, false otherwise
     */
    public boolean hasStripeId() {
        return this.user.getStripeId() != null && !this.user.getStripeId().isEmpty();
    }

    /**
     * Create a Stripe customer if not already created, using provided options.
     *
     * @param <T> the type of user implementing {@link IUserStripe}
     * @param <ID> the type of user ID
     * @param options a map containing optional parameters like name, email, phone, etc.
     * @return String!null the created Stripe customer ID
     * @throws RuntimeException if an error occurs while creating the Stripe customer
     */
    public <T extends IUserStripe, ID> String createAsStripeCustomer(Map<String, Object> options) {
        if (this.hasStripeId()) {
            return this.user.getStripeId();
        }

        if (options == null) {
            options = new HashMap<>();
        }

        if (!options.containsKey("name") && this.user.getName() != null) {
            options.put("name", this.user.getName());
        }

        if (!options.containsKey("email") && this.user.getEmail() != null) {
            options.put("email", this.user.getEmail());
        }

        if (!options.containsKey("phone") && this.user.getPhone() != null) {
            options.put("phone", this.user.getPhone());
        }

        if (!options.containsKey("preferred_locales") && this.user.getPreferredLocales() != null) {
            options.put("preferred_locales", this.user.getPreferredLocales());
        }

        Customer customer;
        try {
            customer = Customer.create(options);
        } catch (StripeException e) {
            throw new RuntimeException("Error creating Stripe customer", e);
        }

        // Assign the Stripe customer ID to this object
        this.user.setStripeId(customer.getId());

        // Optionally save the customer ID in your database, depending on your application's needs
        UserServiceStripe<T, ID> userService = userServiceFactory.create((Class<T>) userClass, (Class<ID>) userId);
        userService.updateStripeId(this.user, this.user.getStripeId());

        return this.user.getStripeId();
    }

    /**
     * Retrieve the Stripe customer object using the Stripe customer ID.
     *
     * @return Customer|null the Stripe customer object or null if not found
     * @throws StripeException if an error occurs while retrieving the customer
     */
    public Customer asStripeCustomer() throws StripeException {
        if (this.user.getStripeId() == null || this.user.getStripeId().isEmpty()) {
            return null;
        }

        return Customer.retrieve(this.user.getStripeId());
    }

    /**
     * Create or retrieve the Stripe customer.
     *
     * @param options a map containing optional parameters like name, email, phone, etc.
     * @return Customer the Stripe customer object
     * @throws StripeException if an error occurs while creating or retrieving the customer
     */
    public Customer createOrGetStripeCustomer(Map<String, Object> options) throws StripeException {
        if (this.hasStripeId()) {
            return this.asStripeCustomer();
        } else {
            this.createAsStripeCustomer(options);
            return this.asStripeCustomer();
        }
    }

    /**
     * Sets the user for the CustomerManager.
     *
     * @param <T> the type of user implementing {@link IUserStripe}
     * @param user the {@link IUserStripe} instance to be set
     * @return the current instance of {@link CustomerManager}
     * @throws RuntimeException if an error occurs while determining the user's ID field
     */
    public <T extends IUserStripe> CustomerManager setUser(T user) {
        this.user = user;
        this.userClass = user.getClass();
        try {
            this.userId = Classes.findIdField(user.getClass()).getType();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Error accessing user ID field", e);
        }
        return this;
    }
}

