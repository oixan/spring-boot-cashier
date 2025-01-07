# Spring-boot-cashier
** WORK IN PROGRESS **

Spring Boot Cashier provides an expressive, fluent interface to Stripe's subscription billing services. It handles almost all of the boilerplate subscription billing code you are dreading writing. In addition to basic subscription management, Spring boot Cashier can handle coupons, swapping subscriptions, subscription "quantities", cancellation grace periods.

## Documentation

## Installation

To install Spring Boot Cashier, follow these steps:

1. Add the interface `IUserStripe` to your User entity:

Here is an example of how to add the `IUserStripe` interface to the `UserEntity` class:

```java
public class UserEntity implements IUserStripe {
  // Your implementation here
}
```

2. Create a concrete repository class similar to this:

```java
@Repository
public interface UserStripeConcreteRepository extends UserStripeRepository<UserEntity, Long> {
}
```

where the first parameter `T` should be the concrete entity class and the second parameter the type of the ID.

3. Initialize a bean to create `UserService` as follows:

```java
@Configuration
public class AppConfigTest {

  @Bean
  public UserService<UserAccount, Long> userService(UserStripeConcreteRepository userStripeConcreteRepository) {
    return new UserService<>(userStripeConcreteRepository);
  }
}
```

4. Add your Stripe API key to the properties file:

```properties
stripe.apiKey=your_stripe_api_key_here
```

## Usage

### Checkout

To handle the checkout process, you can use the following code:

```java
UserEntity user = new UserEntity();
IUserStripeAction userStripe = UserStripeFactory.create(user);

userStripe.customer()
  .createAsStripeCustomer(null);

userStripe.paymentMethod()
  .addPaymentMethod("id");

userStripe.checkout()
  .setPriceId("idprice")
  .setQuantity(1)
  .complete();
```

### Subscription

To manage subscriptions, you can use the following code:

```java
userStripe.subscribe()
  .setPriceId("idprice")
  //.setTrialDay(30)
  .start();
```

### Subscription Service

To interact with the subscription service, you can use the following code:

```java
SubscriptionService subscriptionService = SubscriptionServiceFactory.create();

Optional<Subscription> subscription = subscriptionService.getSubscriptionByUserIdAndType("id", "default");
subscription.ifPresent(s -> System.out.println("Subscription found: " + s.getType()));
```

### Cancel Subscription

To cancel a subscription at the end of the period, you can use the following code:

```java
userStripe.subscription()
    .cancelAtPeriodEnd("default");
```

### Trial Subscription

To check if a subscription is on trial, you can use the following code:

```java
userStripe.subscription()
    .onTrial("default");
```

### Ended Subscription

To check if a subscription has ended, you can use the following code:

```java
userStripe.subscription()
    .ended();
```

## Contributing

We welcome contributions from everyone. Whether it's a bug report, new feature, correction, or additional documentation, your input is appreciated and valued. Please follow our [contributing guidelines](CONTRIBUTING.md) to help us maintain a high standard of quality.


## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.