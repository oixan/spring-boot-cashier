package com.oixan.stripecashier.manager;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

@Configuration
@ComponentScan(basePackages = "com.oixan.stripecashier.manager")
@SpringBootTest(classes = CustomerManager.class)
@TestPropertySource(locations = "classpath:application.properties")
public class CustomerManagerTest {
  
}
