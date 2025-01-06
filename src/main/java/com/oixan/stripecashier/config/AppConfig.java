package com.oixan.stripecashier.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({"com.oixan.stripecashier"})
@EnableJpaRepositories({"com.oixan.stripecashier.repository"})
@EntityScan({"com.oixan.stripecashier.entity"})
@SpringBootApplication
public class AppConfig {
  

   
}
