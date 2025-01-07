package com.oixan.stripecashier.manager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

import com.oixan.stripecashier.builder.StripeBuilder;
import com.oixan.stripecashier.config.AppConfig;
import com.oixan.stripecashier.config.StripeProperties;
import com.oixan.stripecashier.entity.UserAccount;
import com.oixan.stripecashier.factory.UserServiceFactory;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.service.UserService;
import com.stripe.exception.StripeException;


@Configuration
@ComponentScan(basePackages = "com.oixan.stripecashier.*")
@TestPropertySource(locations = "classpath:application.properties", properties = "spring.profiles.active=test")
@SpringBootTest(classes = AppConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerManagerTest {
  
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

        System.out.println("User ID: " + userMock.getId());

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
        Map<String, Object> options = new HashMap<>();
        options.put("description", "Test customer");

        String stripeId = customerManager.createAsStripeCustomer(options);

        assertNotNull(stripeId);
        assertTrue(stripeId.startsWith("cus_"));

        UserService<UserAccount, Long> userService = UserServiceFactory.create(UserAccount.class, Long.class);
        Optional<UserAccount> user = userService.getUserById(userMock);

        assertTrue(user.isPresent(), "User not found");
        assertNotNull(((IUserStripe) user.get()).getStripeId(), "Stripe ID is null");
        assertEquals(stripeId, ((IUserStripe) user.get()).getStripeId(), "Stripe ID does not match");

        //System.out.println("Created Stripe Customer ID: " + stripeId);
    }
	
	@Test
    void testCreateAsStripeCustomer_withExistsStripeId() throws StripeException {
        Map<String, Object> options = new HashMap<>();
        options.put("description", "Test customer");

        userMock.setStripeId("test_stripe_id");
        customerManager.setUser(userMock);
        String stripeId = customerManager.createAsStripeCustomer(options);

        assertNotNull(stripeId);
        assertTrue(stripeId.equals("test_stripe_id"));
    }
}
