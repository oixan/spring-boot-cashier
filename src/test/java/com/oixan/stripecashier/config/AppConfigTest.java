package com.oixan.stripecashier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.oixan.stripecashier.entity.UserAccount;
import com.oixan.stripecashier.repository.UserStripeConcreteRepository;
import com.oixan.stripecashier.service.UserServiceStripe;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfigTest {
	

    @Bean
    public UserServiceStripe<UserAccount, Long> userServiceStripe(UserStripeConcreteRepository userStripeConcreteRepository) {
        UserServiceStripe<UserAccount, Long> userService = new UserServiceStripe<>();
        userService.setRepository(userStripeConcreteRepository);
        return userService;
    }
}
