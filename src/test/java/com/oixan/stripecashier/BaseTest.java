package com.oixan.stripecashier;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import com.oixan.stripecashier.config.AppConfig;
import com.oixan.stripecashier.entity.UserAccount;
import com.oixan.stripecashier.factory.UserServiceFactory;
import com.oixan.stripecashier.factory.UserStripeFactory;
import com.oixan.stripecashier.interfaces.IUserStripeAction;
import com.oixan.stripecashier.service.UserService;
import com.stripe.exception.StripeException;

@Configuration
@ComponentScan(basePackages = "com.oixan.stripecashier.*")
@TestPropertySource(locations = "classpath:application.properties", properties = "spring.profiles.active=test")
@SpringBootTest(classes = AppConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BaseTest {

  protected UserAccount userMock;

  protected IUserStripeAction userActionMock;

  protected Map<String, Object> options;

	@BeforeEach
	protected void setUp() throws StripeException {
        userMock = new UserAccount();
        userMock.setName("John Doe");
        userMock.setEmail("john.doe@example.com");
        userMock.setPhone("1234567890");
        userMock.setPreferredLocales(null);

        userActionMock = UserStripeFactory.create(userMock);

        // Step 1: Initialize PaymentMethodsManager and add a payment method
        options = new HashMap<>();
        options.put("description", "Test deleteing at customer subscription");
        options.put("email", "subscription@live.it");

        UserService<UserAccount, Long> userService = UserServiceFactory.create(UserAccount.class, Long.class);
        userMock = userService.save(userMock);
    }

	 
    @AfterEach
    protected void tearDown() {
        // Esegui una pulizia del database, ad esempio rimuovendo dati di test
        UserService<UserAccount, Long> userService = UserServiceFactory.create(UserAccount.class, Long.class);
        userMock = userService.delete(userMock);
    }
  
}
