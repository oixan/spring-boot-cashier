package com.oixan.stripecashier.manager;

import java.util.HashMap;
import java.util.Map;

import com.oixan.stripecashier.builder.StripeBuilder;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;

public class PaymentMethodsManager {

	CustomerManager customerManager;
	
	StripeBuilder stripeBuilder;
	
	public PaymentMethodsManager(StripeBuilder stripeBuilder) {
		this.stripeBuilder = stripeBuilder;
	}
	
	public PaymentMethodsManager setCustomerManager(CustomerManager customerManager)
	{
		this.customerManager = customerManager;
		return this;
	}
	
	
	public String createSetupIntent() throws StripeException {
	    Map<String, Object> params = new HashMap<>();
	    params.put("customer", customerManager.user.getStripeId());
	    return SetupIntent.create(params).getClientSecret();
	}
	
	
    /**
     * Aggiunge un metodo di pagamento a un cliente Stripe.
     *
     * @param paymentMethodId L'ID del metodo di pagamento Stripe
     * @return Il metodo di pagamento collegato
     * @throws StripeException 
     */
    public PaymentMethod addPaymentMethod(String paymentMethodId) throws StripeException {
        if (assertCustomerExists() == false)
        	throw new IllegalStateException("The client dont have a stripe account");

        PaymentMethod stripePaymentMethod = resolveStripePaymentMethod(paymentMethodId);

        if (!customerManager.user.getStripeId().equals(stripePaymentMethod.getCustomer())) {
            stripePaymentMethod = stripePaymentMethod.attach(Map.of("customer", customerManager.user.getStripeId()));
        }

        return stripePaymentMethod;
    } 
	
    /**
     * Verifica che il cliente esista su Stripe.
     *
     * @throws IllegalStateException Se il cliente non è stato creato su Stripe
     */
    private boolean assertCustomerExists() {
        return customerManager.hasStripeId();
    }
	
    /**
     * Risolve il metodo di pagamento Stripe basandosi sul suo ID.
     *
     * @param paymentMethodId L'ID del metodo di pagamento Stripe
     * @return L'oggetto PaymentMethod da Stripe
     * @throws StripeException Se il metodo di pagamento non può essere recuperato
     */
    private PaymentMethod resolveStripePaymentMethod(String paymentMethodId) throws StripeException {
        return PaymentMethod.retrieve(paymentMethodId);
    }
	
}
