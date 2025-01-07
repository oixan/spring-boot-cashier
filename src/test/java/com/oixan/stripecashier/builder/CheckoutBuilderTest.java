package com.oixan.stripecashier.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import com.oixan.stripecashier.config.StripeProperties;
import com.oixan.stripecashier.entity.UserAccount;
import com.oixan.stripecashier.factory.UserServiceFactory;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;
import com.oixan.stripecashier.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;


@Configuration
@ComponentScan(basePackages = "com.oixan.stripecashier.*")
@TestPropertySource(locations = "classpath:application.properties", properties = "spring.profiles.active=test")
@SpringBootTest(classes = AppConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CheckoutBuilderTest {
  
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
        UserService<UserAccount, Long> userService = UserServiceFactory.create(UserAccount.class, Long.class);
        userMock = userService.delete(userMock);        
        
        System.out.println("Database cleaned.");
    }
	 
	@Test
    void testCreateAsStripeCustomer() throws StripeException {
        // Step 1: Initialize PaymentMethodsManager and add a payment method
        Map<String, Object> options = new HashMap<>();
        options.put("description", "Test customer chekcout");
        options.put("email", "checkout@live.it");
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


        // Step 4: Initialize CheckoutBuilder and create a checkout session
        String checkoutUrl = new CheckoutBuilder(new StripeBuilder(StripeProperties.instance()))
                                    .setUser(userMock)
                                    .setPriceId("price_1QdxAQCtyihjMHctL68So9pU")
                                    .setQuantity(1)
                                    .setSuccessURL("https://example.com/success")
                                    .setCancelURL("https://example.com/cancel")
                                    .complete();

        assertNotNull(checkoutUrl);
        assertThat(checkoutUrl, startsWith("https://checkout.stripe.com/c/pay/"));
    }

}
