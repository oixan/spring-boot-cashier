package com.oixan.stripecashier.manager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.oixan.stripecashier.BaseTest;
import com.oixan.stripecashier.builder.StripeBuilder;
import com.oixan.stripecashier.entity.UserAccount;
import com.oixan.stripecashier.factory.PropertiesFactory;
import com.oixan.stripecashier.factory.UserServiceFactory;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.service.UserService;
import com.stripe.exception.StripeException;

public class CustomerManagerTest extends BaseTest {
  
	private CustomerManager customerManager;

	@BeforeEach
	protected void setUp() throws StripeException {
        super.setUp();

        StripeBuilder stripeBuilder = new StripeBuilder();
        customerManager = new CustomerManager(stripeBuilder);
        customerManager.setUser(userMock);
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
