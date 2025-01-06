package com.oixan.stripecashier.manager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import com.oixan.stripecashier.config.AppConfig;
import com.oixan.stripecashier.factory.UserStripeFactory;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.interfaces.IUserStripeAction;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Subscription;

@Configuration
@ComponentScan(basePackages = "com.oixan.stripecashier.*")
@TestPropertySource(locations = "classpath:application.properties", properties = "spring.profiles.active=test")
@SpringBootTest(classes = AppConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SubscriptionManagerTest {
  
	private IUserStripe userMock;

    private IUserStripeAction userActionMock;
	 
	 @BeforeEach
	 void setUp() throws StripeException {
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

        userActionMock = UserStripeFactory.create(userMock);

        // Step 1: Initialize PaymentMethodsManager and add a payment method
        Map<String, Object> options = new HashMap<>();
        options.put("description", "Test deleteing at customer subscription");
        options.put("email", "subscription@live.it");

        String stripeId = userActionMock.customer().createAsStripeCustomer(options);


        // Step 2: Initialize PaymentMethodsManager and add a payment method
    	// Define a test payment method ID (e.g., a Visa card ID)
    	String firstPaymentMethodId = "pm_card_visa";

    	// Add the payment method and assert it is successfully added
    	PaymentMethod firstPaymentMethod = userActionMock.paymentMethod().addPaymentMethod(firstPaymentMethodId);

    	// Step 3: Set the first payment method as the default
    	userActionMock.paymentMethod().setDefaultPaymentMethod(firstPaymentMethod);
    }
	 
	 
	@Test
    void testSubscriptionCancelAtPeriodEndDefaultType() throws StripeException {
        
        // Step 1: Initialize SubscriptionBuilder and create a subscription
        userActionMock.subscribe()
                    .setPriceId("price_1JMEKICtyihjMHctwnT3KH9g")
                    .start();
       
        // Step 2: Delete the subscription
        Subscription  stripeSubscriptionCancelled = userActionMock.subscription()
                                                                .cancelAtPeriodEnd();

        assertNotNull(stripeSubscriptionCancelled);
        assertTrue(stripeSubscriptionCancelled.getCancelAtPeriodEnd());
        assertTrue(userActionMock.subscription().onGracePeriod());
        assertFalse(userActionMock.subscription().ended());
    }

}
