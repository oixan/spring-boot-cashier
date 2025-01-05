package com.oixan.stripecashier.builder;

import com.oixan.stripecashier.interfaces.IUserStripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;


public class CheckoutBuilder {
	
	/**
     * The StripeBuilder instance used for Stripe operations.
     *
     * @var StripeBuilder
     */
    StripeBuilder stripe;
    

    /**
     * The ID of the price for the product in Stripe.
     *
     * @var String
     */
    String priceId;
    

    /**
     * The quantity of the product to be purchased.
     *
     * @var Long
     */
    Long quantity;
    

    /**
     * The URL to redirect to after a successful payment.
     *
     * @var String
     */
    String successURL;
    

    /**
     * The URL to redirect to if the payment is canceled.
     *
     * @var String
     */
    String cancelURL;
    
    
    IUserStripe user;
    
	
    public CheckoutBuilder(StripeBuilder stripe) {
       this.stripe = stripe;
    }
    
    
    /**
     * Creates a checkout session with the provided price ID, quantity, and URLs.
     * 
     * @return The URL for the checkout session
     * @throws IllegalArgumentException if priceId, successURL, or cancelURL is not set
     */
    public String complete() {
    	 if (priceId == null || successURL == null || cancelURL == null) {
             throw new IllegalArgumentException("priceId, successURL, and cancelURL must be provided.");
         }
    	 
        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity((long) quantity)
                        .setPrice(priceId) 
                        .build();

        SessionCreateParams params =
                SessionCreateParams.builder()
                		.setCustomer(user.getStripeId())
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(successURL)
                        .setCancelUrl(cancelURL)
                        .addLineItem(lineItem)
                        .build();

        Session session;
		try {
			session = Session.create(params);
		} catch (StripeException e) {
			return cancelURL;
		}

        return session.getUrl();
    }
    
    
    /**
     * Sets the price ID for the product.
     * 
     * @param priceId The price ID of the product in Stripe
     * @return The CheckoutBuilder instance for method chaining
     */
    public CheckoutBuilder setPriceId(String priceId) {
    	this.priceId = priceId;
    	return this;
    }
    
    
    /**
     * Sets the quantity of the product.
     * 
     * @param quantity The quantity to be purchased
     * @return The CheckoutBuilder instance for method chaining
     */
    public CheckoutBuilder setQuantity(int quantity) {
    	this.quantity = (long) quantity;
    	return this;
    }
    
    
    /**
     * Sets the success URL for the checkout session.
     * 
     * @param successURL The URL to redirect to after successful payment
     * @return The CheckoutBuilder instance for method chaining
     */
    public CheckoutBuilder setSuccessURL(String successURL) {
    	this.successURL = successURL;
    	return this;
    }
    
    
    /**
     * Sets the cancel URL for the checkout session.
     * 
     * @param cancelURL The URL to redirect to if the payment is canceled
     * @return The CheckoutBuilder instance for method chaining
     */
    public CheckoutBuilder setCancelURL(String cancelURL) {
    	this.cancelURL = cancelURL;
    	return this;
    }
    
    
    public CheckoutBuilder setUser(IUserStripe user) {
    	this.user = user;
    	return this;
    }

}
