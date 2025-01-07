package com.oixan.stripecashier.manager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import com.oixan.stripecashier.config.AppConfig;
import com.oixan.stripecashier.factory.UserServiceFactory;
import com.oixan.stripecashier.factory.UserStripeFactory;
import com.oixan.stripecashier.interfaces.IUserStripeAction;
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
public class SubscriptionManagerTest {
  
	private UserAccount userMock;

    private IUserStripeAction userActionMock;

	@BeforeEach
	 void setUp() throws StripeException {
        userMock = new UserAccount();
        userMock.setName("John Doe");
        userMock.setEmail("john.doe@example.com");
        userMock.setPhone("1234567890");
        userMock.setPreferredLocales(null);

        userActionMock = UserStripeFactory.create(userMock);

        // Step 1: Initialize PaymentMethodsManager and add a payment method
        Map<String, Object> options = new HashMap<>();
        options.put("description", "Test deleteing at customer subscription");
        options.put("email", "subscription@live.it");

        UserService<UserAccount, Long> userService = UserServiceFactory.create(UserAccount.class, Long.class);
        userMock = userService.save(userMock);

        String stripeId = userActionMock.customer().createAsStripeCustomer(options);

        // Step 2: Initialize PaymentMethodsManager and add a payment method
    	// Define a test payment method ID (e.g., a Visa card ID)
    	String firstPaymentMethodId = "pm_card_visa";

    	// Add the payment method and assert it is successfully added
    	PaymentMethod firstPaymentMethod = userActionMock.paymentMethod().addPaymentMethod(firstPaymentMethodId);

    	// Step 3: Set the first payment method as the default
    	userActionMock.paymentMethod().setDefaultPaymentMethod(firstPaymentMethod);
    }
	 
    @AfterEach
    public void tearDown() {
        // Pulizia delle risorse
        System.out.println("Cleaning up after the test...");
        
        // Esegui una pulizia del database, ad esempio rimuovendo dati di test
        UserService<UserAccount, Long> userService = UserServiceFactory.create(UserAccount.class, Long.class);
        userMock = userService.delete(userMock);

        System.out.println("Database cleaned.");
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
