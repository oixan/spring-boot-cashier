package com.oixan.stripecashier.manager;

import java.util.Map;

import com.oixan.stripecashier.builder.StripeBuilder;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

public class CustomerManager {

	IUserStripe user;
	
	StripeBuilder stripeBuilder;
	
	public CustomerManager(StripeBuilder stripeBuilder) {
		this.stripeBuilder = stripeBuilder;
	}
	
	
    /**
     * Retrieve the Stripe customer ID.
     *
     * @return String|null
     */
    public String stripeId() {
        return this.user.getStripeId();
    }

    
    /**
     * Determine if the customer has a Stripe customer ID.
     *
     * @return boolean
     */
    public boolean hasStripeId() {
        return this.user.getStripeId() != null && !this.user.getStripeId().isEmpty();
    }
    
    
    /**
     * Create a Stripe customer if not already created, using provided options.
     *
     * @param options A map containing optional parameters like name, email, phone, etc.
     * @throws StripeException if an error occurs while creating the customer
     * @return String!null the stripe id created
     */
    public String createAsStripeCustomer(Map<String, Object> options){
        if (this.hasStripeId()) {
            return this.user.getStripeId();
        }

        if (!options.containsKey("name") && this.user.getName() != null) {
            options.put("name",  this.user.getName());
        }

        if (!options.containsKey("email") &&  this.user.getEmail() != null) {
            options.put("email", this.user.getEmail());
        }

        if (!options.containsKey("phone") && this.user.getPhone() != null) {
            options.put("phone", this.user.getPhone());
        }

        if (!options.containsKey("address") && this.user.getAddress() != null) {
            options.put("address", this.user.getAddress());
        }

        if (!options.containsKey("preferred_locales") && this.user.getPreferredLocales() != null) {
            options.put("preferred_locales", this.user.getPreferredLocales());
        }

        Customer customer;
		try {
			customer = Customer.create(options);
		} catch (StripeException e) {
			return null;
		}

        // Assign the Stripe customer ID to this object
        this.user.setStripeId(customer.getId());

        // Optionally save the customer ID in your database, depending on your application's needs
        //this.save();
        
        return this.user.getStripeId();
    }
	
    
	public CustomerManager setUser(IUserStripe user) {
		this.user = user;
		return this;
	}
}
