package com.oixan.stripecashier.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import com.oixan.stripecashier.config.AppConfig;
import com.oixan.stripecashier.config.StripeProperties;
import com.oixan.stripecashier.factory.SubscriptionServiceFactory;
import com.oixan.stripecashier.factory.UserServiceFactory;
import com.oixan.stripecashier.factory.UserStripeFactory;
import com.oixan.stripecashier.interfaces.IUserStripeAction;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;
import com.oixan.stripecashier.service.UserService;
import com.oixan.stripecashier.entity.UserAccount;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Subscription;


@Configuration
@ComponentScan(basePackages = "com.oixan.stripecashier.*")
@TestPropertySource(locations = "classpath:application.properties", properties = "spring.profiles.active=test")
@SpringBootTest(classes = AppConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SubscriptionBuilderTest {
  
	 private CustomerManager customerManager;

	 private UserAccount userMock;

	 
	 @BeforeEach
	 void setUp() {
        userMock = new UserAccount();
        userMock.setName("John Doe");
        userMock.setEmail("john.doe@example.com");
        userMock.setPhone("1234567890");
        userMock.setPreferredLocales(null);

        UserService<UserAccount, Long> userService = UserServiceFactory.create(UserAccount.class, Long.class);
        userMock = userService.save(userMock);

        StripeBuilder stripeBuilder = new StripeBuilder(StripeProperties.instance());
        customerManager = new CustomerManager(stripeBuilder);
        customerManager.setUser(userMock);
    }
	 
    @AfterEach
    public void tearDown() {
        // Pulizia delle risorse
        System.out.println("Cleaning up after the test...");
        
        // Esegui una pulizia del database, ad esempio rimuovendo dati di test
        try {
            UserService<UserAccount, Long> userService = UserServiceFactory.create(UserAccount.class, Long.class);
            userMock = userService.delete(userMock);

            System.out.println("Database cleaned.");
        } catch (Exception e) {
            System.err.println("Error cleaning database: " + e.getMessage());
        }
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
        Subscription subscriptionStripe = userStripe.subscribe()
									                  .setPriceId("price_1JMEKICtyihjMHctwnT3KH9g")
									                  .start( null, null, null);

        assertNotNull(subscriptionStripe);                                              
       

        Optional<com.oixan.stripecashier.entity.Subscription> foundSubscription = SubscriptionServiceFactory.create().getSubscriptionByUserIdAndType(stripeId, "default");
        assertTrue(foundSubscription.isPresent());
        assertEquals(subscriptionStripe.getId(), foundSubscription.get().getStripeId());
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
        Subscription subscriptionStripe = userStripe.subscribe()
                                                .setPriceId("price_1JMEKICtyihjMHctwnT3KH9g")
                                                .startAndCreateStripeUserAndPaymentMethod(
                                                    options, null, firstPaymentMethodId, "default");
        
        assertNotNull(subscriptionStripe);

        Optional<com.oixan.stripecashier.entity.Subscription> 
                    foundSubscription = SubscriptionServiceFactory
                                                .create()
                                                .getSubscriptionByUserIdAndType(
                                                    userStripe.getUserStripe().getStripeId(), "default");
                                                    
        assertTrue(foundSubscription.isPresent());
        assertEquals(subscriptionStripe.getId(), foundSubscription.get().getStripeId());
    }

}
