package com.oixan.stripecashier.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import com.oixan.stripecashier.config.StripeProperties;
import com.oixan.stripecashier.factory.UserStripeFactory;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.interfaces.IUserStripeAction;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;

@Configuration
@ComponentScan(basePackages = "com.oixan.stripecashier.manager")
@TestPropertySource(locations = "classpath:application.properties")
public class SubscriptionBuilderTest {
  
	 private CustomerManager customerManager;

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
    }
	 
	  @Test
    void testCreateSubscriptionExistingCustomerAndDefaultPaymentMethod() throws StripeException {
        // Step 1: Initialize PaymentMethodsManager and add a payment method
        Map<String, Object> options = new HashMap<>();
        options.put("description", "Test existing customer new subscription");
        options.put("email", "subscription@live.it");
        String stripeId = customerManager
                                .setUser(userMock)
                                .createAsStripeCustomer(options);
        assertNotNull(stripeId);

        // Step 2: Initialize PaymentMethodsManager and add a payment method
    	PaymentMethodsManager paymentMethodsManager = new PaymentMethodsManager(new StripeBuilder(StripeProperties.instance()))
    	    .setCustomerManager(customerManager);

    	// Define a test payment method ID (e.g., a Visa card ID)
    	String firstPaymentMethodId = "pm_card_visa";

    	// Add the payment method and assert it is successfully added
    	PaymentMethod firstPaymentMethod = paymentMethodsManager.addPaymentMethod(firstPaymentMethodId);

    	// Step 3: Set the first payment method as the default
    	paymentMethodsManager.setDefaultPaymentMethod(firstPaymentMethod);


        // Step 4: Initialize SubscriptionBuilder and create a subscription
        IUserStripeAction userStripe = UserStripeFactory.create(userMock);
        userStripe.subscribe()
                  .setPriceId("price_1JMEKICtyihjMHctwnT3KH9g")
                  .start( null, null);
    }


    @Test
    void testCreateSubscriptionNewCustomerAndNewPaymentMethod() throws StripeException {
        // Step 1: Initialize PaymentMethodsManager and add a payment method
        Map<String, Object> options = new HashMap<>();
        options.put("description", "Test new customer with new subscription");
        options.put("email", "subscription@live.it");

        // Define a test payment method ID (e.g., a Visa card ID)
        String firstPaymentMethodId = "pm_card_visa";

        // Step 2: Initialize SubscriptionBuilder and create a new user and new subscription
        IUserStripeAction userStripe = UserStripeFactory.create(userMock);
        userStripe.subscribe()
                  .setPriceId("price_1JMEKICtyihjMHctwnT3KH9g")
                  .startAndCreateStripeUserAndPaymentMethod(options, null, firstPaymentMethodId);
    }

}
