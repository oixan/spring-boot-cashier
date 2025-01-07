package com.oixan.stripecashier.interfaces;

public interface IUserStripe
{	
    String getStripeId();
    void setStripeId(String stripeId);

    String getName();
    void setName(String name);

    String getEmail();
    void setEmail(String email);

    String getPhone();
    void setPhone(String phone);

    /*/
    String getAddress();
    void setAddress(String address);
    */
    
    String getPreferredLocales();
    void setPreferredLocales(String preferredLocales);
}