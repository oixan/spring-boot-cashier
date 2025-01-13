package com.oixan.stripecashier.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oixan.stripecashier.builder.StripeBuilder;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;

/**
 * Manages payment methods for a Stripe customer, including creation, deletion, and setting default methods.
 */
@Component
public class PaymentMethodsManager {

    CustomerManager customerManager;

    @Autowired
    StripeBuilder stripeBuilder;

    /**
     * Constructor for PaymentMethodsManager.
     */
    public PaymentMethodsManager() {
    }

    /**
     * Sets the    instance.
     *
     * @param customerManager An instance of   
     * @return The current instance of {@link PaymentMethodsManager}
     */
    public PaymentMethodsManager setCustomerManager(CustomerManager customerManager) {
        this.customerManager = customerManager;
        return this;
    }

    /**
     * Creates a SetupIntent for the customer.
     *
     * @return The client secret of the created SetupIntent
     * @throws StripeException If an error occurs while creating the SetupIntent
     */
    public String createSetupIntent() throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerManager.user.getStripeId());
        return SetupIntent.create(params).getClientSecret();
    }

    /**
     * Adds a payment method to a Stripe customer.
     *
     * @param paymentMethodId The ID of the Stripe payment method
     * @return The attached {@link PaymentMethod}
     * @throws StripeException If an error occurs while attaching the payment method
     */
    public PaymentMethod addPaymentMethod(String paymentMethodId) throws StripeException {
        if (assertCustomerExists() == false)
            throw new IllegalStateException("The client does not have a Stripe account");

        PaymentMethod stripePaymentMethod = resolveStripePaymentMethod(paymentMethodId);

        if (!customerManager.user.getStripeId().equals(stripePaymentMethod.getCustomer())) {
            stripePaymentMethod = stripePaymentMethod.attach(Map.of("customer", customerManager.user.getStripeId()));
        }

        return stripePaymentMethod;
    }

    /**
     * Deletes a payment method by detaching it from the customer.
     * If the payment method is the default and there is only one payment method, it will not be deleted.
     * If there are multiple payment methods, it will be deleted and another will be set as default.
     *
     * @param paymentMethodId The Stripe PaymentMethod ID to be removed
     * @return {@code true} if the payment method was successfully removed, {@code false} otherwise
     * @throws StripeException If the Stripe API call fails
     */
    public boolean deletePaymentMethod(String paymentMethodId) throws StripeException {
        if (assertCustomerExists() == false) {
            throw new IllegalStateException("The client does not have a Stripe account");
        }

        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);

        if (!paymentMethod.getCustomer().equals(customerManager.user.getStripeId())) {
            throw new IllegalStateException("The payment method is not attached to the correct customer.");
        }

        List<PaymentMethod> paymentMethods = paymentMethods();
        if (paymentMethods.size() == 1) {
            throw new IllegalStateException("Cannot delete the only payment method.");
        }

        PaymentMethod defaultPaymentMethod = defaultPaymentMethod();
        if (defaultPaymentMethod != null && defaultPaymentMethod.getId().equals(paymentMethodId)) {
            for (PaymentMethod pm : paymentMethods) {
                if (!pm.getId().equals(paymentMethodId)) {
                    setDefaultPaymentMethod(pm);
                    break;
                }
            }
        }

        paymentMethod = paymentMethod.detach();

        return paymentMethod.getId().equals(paymentMethodId);
    }

    /**
     * Retrieves the list of payment methods for the customer from Stripe.
     *
     * @return A list of {@link PaymentMethod}
     * @throws StripeException If the Stripe API call fails
     */
    public List<PaymentMethod> paymentMethods() throws StripeException {
        if (assertCustomerExists() == false) {
            throw new IllegalStateException("The client does not have a Stripe account");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerManager.user.getStripeId());
        params.put("type", "card");

        return PaymentMethod.list(params).getData();
    }

    /**
     * Sets the provided payment method as the default payment method for the customer.
     *
     * @param paymentMethod The {@link PaymentMethod} to be set as default
     * @throws StripeException If an error occurs while updating the customer
     */
    public void setDefaultPaymentMethod(PaymentMethod paymentMethod) throws StripeException {
        Customer customer = Customer.retrieve(customerManager.user.getStripeId());

        customer.update(
            Map.of("invoice_settings", Map.of("default_payment_method", paymentMethod.getId()))
        );
    }

    /**
     * Gets the default payment method for the customer.
     *
     * @return The default {@link PaymentMethod} or {@code null} if none is set
     * @throws StripeException If an error occurs while retrieving the customer or payment method
     */
    public PaymentMethod defaultPaymentMethod() throws StripeException {
        if (!this.customerManager.hasStripeId()) {
            return null;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("expand", List.of("invoice_settings.default_payment_method"));
        Customer customer = Customer.retrieve(this.customerManager.user.getStripeId(), params, null);

        if (customer.getInvoiceSettings().getDefaultPaymentMethod() != null) {
            String defaultPaymentMethodId = customer.getInvoiceSettings().getDefaultPaymentMethod();
            return PaymentMethod.retrieve(defaultPaymentMethodId);
        }

        return null;
    }

    /**
     * Verifies that the customer exists on Stripe.
     *
     * @return {@code true} if the customer exists, {@code false} otherwise
     */
    private boolean assertCustomerExists() {
        return customerManager.hasStripeId();
    }

    /**
     * Resolves the Stripe payment method based on its ID.
     *
     * @param paymentMethodId The ID of the Stripe payment method
     * @return The {@link PaymentMethod} object from Stripe
     * @throws StripeException If the payment method cannot be retrieved
     */
    private PaymentMethod resolveStripePaymentMethod(String paymentMethodId) throws StripeException {
        return PaymentMethod.retrieve(paymentMethodId);
    }
}
