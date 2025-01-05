package com.oixan.stripecashier.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import com.oixan.stripecashier.builder.StripeBuilder;
import com.oixan.stripecashier.config.StripeProperties;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;

@Configuration
@ComponentScan(basePackages = "com.oixan.stripecashier.manager")
@TestPropertySource(locations = "classpath:application.properties")
public class PaymentMethodsManagerTest {
  
	 private CustomerManager customerManager;

	 private PaymentMethodsManager paymentMethodsManager;

	 private IUserStripe userMock;
	 
	 @BeforeEach
	 void setUp() {
        userMock = new IUserStripe() {
            private String stripeId;

            @Override
            public String getStripeId() {
                return stripeId;
            }

            @Override
            public void setStripeId(String stripeId) {
                this.stripeId = stripeId;
            }

            @Override
            public String getName() {
                return "John Doe";
            }

            @Override
            public String getEmail() {
                return "john.doe@example.com";
            }

            @Override
            public String getPhone() {
                return "1234567890";
            }

            @Override
            public String getAddress() {
                return null;
            }

            @Override
            public String getPreferredLocales() {
                return null;
            }

			@Override
			public void setName(String name) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setEmail(String email) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setPhone(String phone) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setAddress(String address) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setPreferredLocales(String preferredLocales) {
				// TODO Auto-generated method stub
				
			}
        };

        StripeBuilder stripeBuilder = new StripeBuilder(StripeProperties.instance());
        customerManager = new CustomerManager(stripeBuilder);
        customerManager.setUser(userMock);
        
        paymentMethodsManager = new PaymentMethodsManager(
								   	new StripeBuilder(StripeProperties.instance())
								   )
								   .setCustomerManager(customerManager);
    }
	 
	@Test
    void testAddPaymentMethodOnCustomer() throws StripeException {
		  	Map<String, Object> options = new HashMap<>();
		    options.put("description", "Test customer addPaymentMethods");
		    String stripeCustomerId = customerManager.createAsStripeCustomer(options);
		    assertNotNull(stripeCustomerId, "Stripe customer ID should not be null");

		    
		    String paymentMethodId = "pm_card_visa";
		    PaymentMethod attachedPaymentMethod = paymentMethodsManager.addPaymentMethod(paymentMethodId);
		    
		    
		    assertNotNull(attachedPaymentMethod, "Attached PaymentMethod should not be null");
		    assertTrue(attachedPaymentMethod.getCustomer().equals(stripeCustomerId), 
		        "Payment method should be attached to the correct customer");
    }
	
	
	@Test
	void testAddSecondPaymentMethodOnCustomer() throws StripeException {
	    Map<String, Object> options = new HashMap<>();
	    options.put("description", "Test customer addSecondPaymentMethod");
	    String stripeCustomerId = customerManager.createAsStripeCustomer(options);
	    assertNotNull(stripeCustomerId, "Stripe customer ID should not be null");
	    

	    String firstPaymentMethodId = "pm_card_visa"; 
	    PaymentMethod firstPaymentMethod = paymentMethodsManager.addPaymentMethod(firstPaymentMethodId);
	    assertNotNull(firstPaymentMethod, "First PaymentMethod should not be null");
	    assertTrue(firstPaymentMethod.getCustomer().equals(stripeCustomerId), 
	        "First payment method should be attached to the correct customer");
	    

	    String secondPaymentMethodId = "pm_card_mastercard";
	    PaymentMethod secondPaymentMethod = paymentMethodsManager.addPaymentMethod(secondPaymentMethodId);
	    assertNotNull(secondPaymentMethod, "Second PaymentMethod should not be null");
	    assertTrue(secondPaymentMethod.getCustomer().equals(stripeCustomerId), 
	        "Second payment method should be attached to the correct customer");

	    
	    Map<String, Object> params = new HashMap<>();
	    params.put("customer", stripeCustomerId);
	    params.put("type", "card");

	    PaymentMethodCollection paymentMethods = PaymentMethod.list(params);
	    assertTrue(paymentMethods.getData().size() >= 2, 
	        "Customer should have at least two payment methods");

	    // Output per debug
	    System.out.println("First Payment Method: " + firstPaymentMethod.getId());
	    System.out.println("Second Payment Method: " + secondPaymentMethod.getId());
	}

	
    @Test
    void testDefaultPaymentMethod_withValidMethod() throws StripeException {
    	// Step 1: Create a Stripe customer with a description
    	Map<String, Object> customerOptions = new HashMap<>();
    	customerOptions.put("description", "Test customer paymentMethod default");

    	// Create the customer and retrieve the customer ID
    	String stripeCustomerId = customerManager.createAsStripeCustomer(customerOptions);
    	assertNotNull(stripeCustomerId, "Stripe customer ID should not be null");

    	// Step 2: Initialize PaymentMethodsManager and add a payment method
    	PaymentMethodsManager paymentMethodsManager = new PaymentMethodsManager(new StripeBuilder(StripeProperties.instance()))
    	    .setCustomerManager(customerManager);

    	// Define a test payment method ID (e.g., a Visa card ID)
    	String firstPaymentMethodId = "pm_card_visa";

    	// Add the payment method and assert it is successfully added
    	PaymentMethod firstPaymentMethod = paymentMethodsManager.addPaymentMethod(firstPaymentMethodId);
    	assertNotNull(firstPaymentMethod, "First PaymentMethod should not be null");
    	assertTrue(firstPaymentMethod.getCustomer().equals(stripeCustomerId), 
    	    "First payment method should be attached to the correct customer");

    	// Step 3: Set the first payment method as the default
    	paymentMethodsManager.setDefaultPaymentMethod(firstPaymentMethod);

    	// Step 4: Verify that the default payment method is correctly set
    	PaymentMethod defaultPaymentMethod = paymentMethodsManager.defaultPaymentMethod();
    	assertNotNull(defaultPaymentMethod, "Default PaymentMethod should not be null");
    	assertEquals(firstPaymentMethod.getId(), defaultPaymentMethod.getId(), 
    	    "The payment method should match the default one.");
    }
    

    @Test
    void testDefaultPaymentMethod_withNoDefaultPaymentMethod() throws StripeException {
        // Step 1: Create a Stripe customer with a description
        Map<String, Object> customerOptions = new HashMap<>();
        customerOptions.put("description", "Test customer paymentMethod with no default");

        // Create the customer and retrieve the customer ID
        String stripeCustomerId = customerManager.createAsStripeCustomer(customerOptions);
        assertNotNull(stripeCustomerId, "Stripe customer ID should not be null");

        // Step 2: Initialize PaymentMethodsManager and add a payment method
        PaymentMethodsManager paymentMethodsManager = new PaymentMethodsManager(new StripeBuilder(StripeProperties.instance()))
            .setCustomerManager(customerManager);

        // Define a test payment method ID (e.g., a Visa card ID)
        String firstPaymentMethodId = "pm_card_visa";

        // Add the payment method and assert it is successfully added
        PaymentMethod firstPaymentMethod = paymentMethodsManager.addPaymentMethod(firstPaymentMethodId);
        assertNotNull(firstPaymentMethod, "First PaymentMethod should not be null");
        assertTrue(firstPaymentMethod.getCustomer().equals(stripeCustomerId), 
            "First payment method should be attached to the correct customer");

        // Step 3: Ensure the payment method is not set as default initially
        PaymentMethod currentDefaultPaymentMethod = paymentMethodsManager.defaultPaymentMethod();
        assertNull(currentDefaultPaymentMethod, "No default payment method should be set initially");

        // Step 4: Set the first payment method as the default
        paymentMethodsManager.setDefaultPaymentMethod(firstPaymentMethod);

        // Step 5: Verify that the first payment method is now set as the default
        PaymentMethod defaultPaymentMethod = paymentMethodsManager.defaultPaymentMethod();
        assertNotNull(defaultPaymentMethod, "Default PaymentMethod should not be null");
        assertEquals(firstPaymentMethod.getId(), defaultPaymentMethod.getId(), 
            "The payment method should match the default one.");
    }
	
}
