package com.oixan.stripecashier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.oixan.stripecashier.entity.UserAccount;
import com.oixan.stripecashier.repository.UserStripeConcreteRepository;
import com.oixan.stripecashier.service.UserService;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfigTest {

    @Bean
    public UserService<UserAccount, Long> userService(UserStripeConcreteRepository userStripeConcreteRepository) {
        return new UserService<>(userStripeConcreteRepository);
    }
}
