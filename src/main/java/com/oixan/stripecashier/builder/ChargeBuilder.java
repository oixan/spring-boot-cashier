package com.oixan.stripecashier.builder;

import com.oixan.stripecashier.factory.PaymentMethodsManagerFactory;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code ChargeBuilder} class is responsible for building and managing payment charges using the Stripe API.
 * <p>
 * This class provides methods to set the user associated with the checkout session, perform payment transactions,
 * and handle refunds. It utilizes the {@code StripeBuilder} and {@code PaymentMethodsManagerFactory} components
 * to facilitate the payment process.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * ChargeBuilder chargeBuilder = new ChargeBuilder();
 * chargeBuilder.setUser(user);
 * PaymentIntent paymentIntent = chargeBuilder.pay(100.0);
 * }
 * </pre>
 * <p>
 * The class supports method chaining for setting up the payment process.
 * <p>
 * Note: This class requires proper configuration of Stripe API keys and other dependencies.
 * 
 * @see StripeBuilder
 * @see PaymentMethodsManagerFactory
 * @see IUserStripe
 * @see PaymentIntent
 * @see Refund
 */
@Component
public class ChargeBuilder {

    /**
     * The payment method to be used for the charge.
     */
    @Autowired
    private StripeBuilder stripeBuilder;


    /**
     * The payment method to be used for the charge.
     */
    @Autowired
    private PaymentMethodsManagerFactory paymentMethodsManagerFactory;


    /**
     * The user associated with the checkout session.
     */
    private IUserStripe user; 


    /**
     * Constructs a new {@code ChargeBuilder} instance.
     */
    public ChargeBuilder() {
    }


    /**
     * Sets the user associated with the checkout session.
     *
     * @param user the user implementing {@code IUserStripe}
     * @return the {@code CheckoutBuilder} instance for method chaining
     */
    public ChargeBuilder setUser(IUserStripe user) {
      this.user = user;
      return this;
    }


    /**
     * Charges the customer using the default provided payment method.
     * <p>
     * This method performs a payment transaction using the provided amount.
     * If no additional options are specified, the default payment method will be used.
     * 
     * @param amount The amount to be charged to the customer. It must be a positive value.
     * @return The PaymentIntent object representing the result of the payment attempt.
     * @throws Exception If there is an error during the payment process.
     */
    public PaymentIntent pay(double amount) throws Exception {
      return pay(amount, null);
    }
    
      
    /**
     * Charges the customer using the default provided payment method.
     * <p>
     * This method performs a payment transaction using the provided amount.
     * If no additional options are specified, the default payment method will be used.
     * 
     * @param amount The amount to be charged to the customer. It must be a positive value.
     * @param options A map of additional options for configuring the payment. This can include settings such as
     *                confirmation methods and payment method preferences. If null, default options will be used.     
     * @return A PaymentIntent object representing the result of the payment attempt.
     *         This object contains information about the payment status and transaction details.
     * @throws Exception If there is an error during the payment process, such as a failed transaction or invalid parameters.
     */
    public PaymentIntent pay(double amount, Map<String, Object> options) throws Exception {
      PaymentMethod paymentMethod = paymentMethodsManagerFactory.create(user).defaultPaymentMethod();

      if (paymentMethod == null) {
          throw new Exception("No default payment method found.");
      }

      return pay(amount, paymentMethod.getId(), options);
    }


    /**
     * Charges the customer using the default provided payment method.
     * <p>
     * This method performs a payment transaction using the provided amount.
     * If no additional options are specified, the default payment method will be used.
     * 
     * @param amount The amount to be charged to the customer. It must be a positive value.
     * @param paymentMethod The identifier for the payment method to be used (e.g., a Stripe payment method ID).
     * @param options A map of additional options for configuring the payment. This can include settings such as
     *                confirmation methods and payment method preferences. If null, default options will be used.
     * @return A PaymentIntent object representing the result of the payment attempt.
     *         This object contains information about the payment status and transaction details.
     * @throws Exception If there is an error during the payment process, such as a failed transaction or invalid parameters.
     */
    public PaymentIntent pay(double amount, String paymentMethod, Map<String, Object> options) throws Exception {
        if (options == null)
          options = new HashMap<>();

        //options.put("confirmation_method", "automatic");
        options.put("confirm", true);
        options.put("payment_method", paymentMethod);

        options.put("automatic_payment_methods", Map.of("enabled", true, "allow_redirects", "never"));

        // Create the PaymentIntent
        PaymentIntent paymentIntent = createPayment(amount, options, user.getStripeId());

        // Validate the payment (you can implement your validation logic here)
        validate(paymentIntent);

        return paymentIntent;
    }

    /**
     * Refund a customer for a charge.
     * 
     * @param paymentIntentId the ID of the PaymentIntent to refund
     * @return the created Refund object
     * @throws Exception If there is an error during the refaund process, such as a failed transaction or invalid parameters.
     */
    public Refund refund(String paymentIntentId) throws Exception {
      return refund(paymentIntentId, null);
    }

    /**
     * Refund a customer for a charge.
     * 
     * @param paymentIntentId the ID of the PaymentIntent to refund
     * @param options optional parameters for the refund
     * @return the created Refund object
     * @throws Exception If there is an error during the refund process, such as a failed transaction or invalid parameters.
     */
    public Refund refund(String paymentIntentId, Map<String, Object> options) throws Exception {
        if (options == null)
            options = new HashMap<>();

        // Aggiungi il PaymentIntent come parametro
        options.put("payment_intent", paymentIntentId);

        // Imposta i parametri per la creazione del rimborso
        RefundCreateParams.Builder paramsBuilder = RefundCreateParams.builder();

        // Aggiungi gli altri parametri
        options.forEach((key, value) -> paramsBuilder.putExtraParam(key, value));

        try {
            // Crea il rimborso
            Refund refund = Refund.create(paramsBuilder.build());
            return refund;
        } catch (Exception e) {
            throw new Exception("Failed to create Refund: " + e.getMessage(), e);
        }
    }


    /**
     * Creates a PaymentIntent using the provided options.
     * 
     * @param amount The amount to be charged, in the smallest currency unit (e.g., cents).
     * @param options Additional options for creating the PaymentIntent.
     * @param stripeId The Stripe ID of the customer.
     * @return The created PaymentIntent object.
     * @throws Exception If an error occurs during PaymentIntent creation.
     */
    private PaymentIntent createPayment(double amount, Map<String, Object> options, String stripeId) throws Exception {
    
      // Check if the customer has a Stripe ID and add it to the options
      if (hasStripeId(stripeId)) {
          options.put("customer", stripeId);
      }

      // Create a PaymentIntent using the provided options
      PaymentIntentCreateParams.Builder paramsBuilder = PaymentIntentCreateParams.builder()
              .setAmount((long) Math.round(amount * 100))
              .setCurrency((String) preferredCurrency());

      // Add other options (like confirmation_method, payment_method, etc.)
      options.forEach(paramsBuilder::putExtraParam);

      try {
          // Create the PaymentIntent
          PaymentIntent paymentIntent = PaymentIntent.create(paramsBuilder.build());
          return paymentIntent;
      } catch (Exception e) {
          throw new Exception("Failed to create PaymentIntent: " + e.getMessage(), e);
      }
    }

    
    /**
     * Returns the preferred currency for the payment.
     * 
     * @return
     */
    private String preferredCurrency() {
        return stripeBuilder.getCurrency();
    }


    /**
     * Checks if the customer has a Stripe ID.
     * 
     * @param stripeId
     * @return
     */
    private boolean hasStripeId(String stripeId) {
        return stripeId != null && !stripeId.isEmpty();
    }


    /**
     * Validates the PaymentIntent.
     * 
     * @param paymentIntent
     * @throws Exception
     */
    private void validate(PaymentIntent paymentIntent) throws Exception {
        if (paymentIntent.getPaymentMethod() == null) {
            throw new Exception("Payment method is required for this payment.");
        }

        if ("requires_action".equals(paymentIntent.getStatus())) {
            throw new Exception("This payment requires additional action.");
        }

        if ("requires_confirmation".equals(paymentIntent.getStatus())) {
            throw new Exception("This payment requires confirmation.");
        }
    }
}
