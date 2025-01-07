package com.oixan.stripecashier.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.oixan.stripecashier.entity.UserAccount;
import com.oixan.stripecashier.repository.UserStripeConcreteRepository;
import com.oixan.stripecashier.service.UserService;

@ComponentScan({"com.oixan.stripecashier"})
@EnableJpaRepositories({"com.oixan.stripecashier.repository"})
@EntityScan({"com.oixan.stripecashier.entity"})
@SpringBootApplication
public class AppConfig {
  
    @Bean
    public UserService<UserAccount, Long> userService(UserStripeConcreteRepository userStripeConcreteRepository) {
        return new UserService<>(userStripeConcreteRepository);
    }
   
}
