package com.oixan.stripecashier.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oixan.stripecashier.interfaces.IUserStripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;


/**
 * The {@code CheckoutBuilder} class is responsible for creating and configuring a Stripe checkout session.
 * It allows setting various parameters such as price ID, quantity, success URL, cancel URL, and user.
 * The {@code complete} method finalizes the checkout session creation and returns the session URL.
 */
@Component
public class CheckoutBuilder {


    /**
     * The ID of the price for the product in Stripe.
     */
    private String priceId;

    /**
     * The quantity of the product to be purchased.
     */
    private Long quantity;

    /**
     * The URL to redirect to after a successful payment.
     */
    private String successURL;

    /**
     * The URL to redirect to if the payment is canceled.
     */
    private String cancelURL;

    /**
     * The user associated with the checkout session.
     */
    private IUserStripe user;

    /**
     * Constructs a new {@code CheckoutBuilder} instance with the specified   .
     *
     */
    public CheckoutBuilder() {
    }

    /**
     * Finalizes the checkout session creation and returns the session URL.
     *
     * @return the URL for the checkout session
     * @throws IllegalArgumentException if {@code priceId}, {@code successURL}, or {@code cancelURL} is not set
     */
    public String complete() {
        if (priceId == null || successURL == null || cancelURL == null) {
            throw new IllegalArgumentException("priceId, successURL, and cancelURL must be provided.");
        }

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(quantity)
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

        try {
            Session session = Session.create(params);
            return session.getUrl();
        } catch (StripeException e) {
            throw new IllegalArgumentException("Failed to create checkout session.", e);
        }
    }

    /**
     * Sets the price ID for the product.
     *
     * @param priceId the price ID of the product in Stripe
     * @return the {@code CheckoutBuilder} instance for method chaining
     */
    public CheckoutBuilder setPriceId(String priceId) {
        this.priceId = priceId;
        return this;
    }

    /**
     * Sets the quantity of the product.
     *
     * @param quantity the quantity to be purchased
     * @return the {@code CheckoutBuilder} instance for method chaining
     */
    public CheckoutBuilder setQuantity(int quantity) {
        this.quantity = (long) quantity;
        return this;
    }

    /**
     * Sets the success URL for the checkout session.
     *
     * @param successURL the URL to redirect to after a successful payment
     * @return the {@code CheckoutBuilder} instance for method chaining
     */
    public CheckoutBuilder setSuccessURL(String successURL) {
        this.successURL = successURL;
        return this;
    }

    /**
     * Sets the cancel URL for the checkout session.
     *
     * @param cancelURL the URL to redirect to if the payment is canceled
     * @return the {@code CheckoutBuilder} instance for method chaining
     */
    public CheckoutBuilder setCancelURL(String cancelURL) {
        this.cancelURL = cancelURL;
        return this;
    }

    /**
     * Sets the user associated with the checkout session.
     *
     * @param user the user implementing {@code IUserStripe}
     * @return the {@code CheckoutBuilder} instance for method chaining
     */
    public CheckoutBuilder setUser(IUserStripe user) {
        this.user = user;
        return this;
    }
}
