package com.oixan.stripecashier.interfaces;

/**
 * The {@code IUserStripe} interface defines the contract for user objects 
 * that interact with the Stripe API.
 * <p>
 * Classes implementing this interface represent user entities with 
 * essential information required for Stripe integrations, such as 
 * Stripe ID, name, email, phone, and preferred locales.
 */
public interface IUserStripe {

    /**
     * Gets the user's Stripe ID.
     *
     * @return the Stripe ID of the user
     */
    String getStripeId();

    /**
     * Sets the user's Stripe ID.
     *
     * @param stripeId the Stripe ID to set
     */
    void setStripeId(String stripeId);

    /**
     * Gets the user's name.
     *
     * @return the name of the user
     */
    String getName();

    /**
     * Sets the user's name.
     *
     * @param name the name to set
     */
    void setName(String name);

    /**
     * Gets the user's email address.
     *
     * @return the email address of the user
     */
    String getEmail();

    /**
     * Sets the user's email address.
     *
     * @param email the email address to set
     */
    void setEmail(String email);

    /**
     * Gets the user's phone number.
     *
     * @return the phone number of the user
     */
    String getPhone();

    /**
     * Sets the user's phone number.
     *
     * @param phone the phone number to set
     */
    void setPhone(String phone);

    /**
     * Gets the user's preferred locales.
     * <p>
     * Preferred locales are typically used to determine the language 
     * or regional settings for Stripe interactions.
     *
     * @return the preferred locales of the user
     */
    String getPreferredLocales();

    /**
     * Sets the user's preferred locales.
     *
     * @param preferredLocales the preferred locales to set
     */
    void setPreferredLocales(String preferredLocales);

    /**
     * (Optional) Gets the user's address.
     * <p>
     * This method is commented out and can be enabled if needed 
     * for additional user information.
     *
     * @return the address of the user
     */
    // String getAddress();

    /**
     * (Optional) Sets the user's address.
     * <p>
     * This method is commented out and can be enabled if needed 
     * for additional user information.
     *
     * @param address the address to set
     */
    // void setAddress(String address);
}
