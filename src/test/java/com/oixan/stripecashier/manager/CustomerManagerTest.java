package com.oixan.stripecashier.manager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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

@Configuration
@ComponentScan(basePackages = "com.oixan.stripecashier.manager")
@TestPropertySource(locations = "classpath:application.properties")
public class CustomerManagerTest {
  
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
    void testCreateAsStripeCustomer() throws StripeException {
        Map<String, Object> options = new HashMap<>();
        options.put("description", "Test customer");

        String stripeId = customerManager.createAsStripeCustomer(options);

        assertNotNull(stripeId);
        assertTrue(stripeId.startsWith("cus_"));

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
