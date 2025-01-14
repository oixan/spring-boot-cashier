# Spring-boot-cashier
** WORK IN PROGRESS **

Spring Boot Cashier provides an expressive, fluent interface to Stripe's subscription billing services. It handles almost all of the boilerplate subscription billing code you are dreading writing. In addition to basic subscription management, Spring boot Cashier can handle coupons, swapping subscriptions, subscription "quantities", cancellation grace periods.

## Documentation

## Installation

To install Spring Boot Cashier, follow these steps:

To install the Spring Boot Cashier package, you can use either Maven or Gradle.

### Maven

Add the following dependency to your `pom.xml` file:

```xml
<dependency>
  <groupId>io.github.oixan</groupId>
  <artifactId>spring-boot-cashier</artifactId>
  <version>0.1.1</version>
</dependency>
```

### Gradle

Add the following implementation to your `build.gradle` file:

```groovy
implementation group: 'io.github.oixan', name: 'spring-boot-cashier', version: '0.1.1'
```

### 1. Add the interface `IUserStripe` to your User entity:

Here is an example of how to add the `IUserStripe` interface to the `UserEntity` class:

```java
public class UserEntity implements IUserStripe {
  ...

    @Column(name = "stripe_id", nullable = true, unique = true)
    private String stripeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "preferred_locales")
    private String preferredLocales;

  ...
}
```

### 2. Create a concrete repository class similar to this:

```java
@Repository
public interface UserStripeConcreteRepository extends UserStripeRepository<UserEntity, Long> {
}
```

where the first parameter `T` should be the concrete entity class and the second parameter the type of the ID.

### 3. Initialize a bean to create `UserService` as follows:

```java
@Configuration
public class AppConfigTest {

	  @Bean
	  public UserServiceStripe<User, Long> userService(UserStripeConcreteRepository userStripeConcreteRepository) {
		    UserServiceStripe<User, Long> userService = new UserServiceStripe<>();
	      userService.setRepository(userStripeConcreteRepository);
	      return userService;
	  }
}
```

### 4. Add your Stripe API key to the properties file:

```properties
stripe.apiKey=your_stripe_api_key_here
```

## Usage

### Customer

To create a Stripe customer from a user that implements `IUserStripe`, you can use the following code:

```java
@Autowired 
UserStripeFactory userStripeFactory;

UserEntity user = new UserEntity();
IUserStripeAction userStripe = userStripeFactory.create(user)

userStripe.customer()
  .createAsStripeCustomer(null);

userStripe.customer()
  .createOrGetStripeCustomer(null);
```

To get a Existing Sripte customer you can use the following code:

```
userStripe.customer()
  .asStripeCustomer();
```


### PaymentMethod

To manage payment methods, you can use the following code:

```java
PaymentMethod pm = userStripe.paymentMethod()
  .addPaymentMethod("pm_card_visa");

userStripe.paymentMethod().setDefaultPaymentMethod(pm);
```

### Checkout

To handle the checkout process, you can use the following code:

```java
@Autowired 
UserStripeFactory userStripeFactory;


UserEntity user = new UserEntity();
IUserStripeAction userStripe = userStripeFactory.create(user);

userStripe.customer()
  .createAsStripeCustomer(null);

userStripe.paymentMethod()
  .addPaymentMethod("id");

userStripe.checkout()
  .setPriceId("idprice")
  .setQuantity(1)
  .setCancelURL("https://www.test.it/cancel")
	.setSuccessURL("https://www.test.it/success")
  .complete();
```

### Charges

#### Charge

You can also handle charge single payment made by the customer using Stripe.

```java
  PaymentIntent paymentIntent = userStripe.charge()
                                             .pay(25.99);
```

#### Refaunds

You can also handle refunds for a payment made by the customer using Stripe.

```java
userStripe.charge()
            .refund(paymentIntent.getId());
```

### Subscription

To manage subscriptions, you can use the following code:

```java
userStripe.subscribe()
  .setPriceId("idprice")
  //.setTrialDay(30)
  .start();
```

#### Swap Subscription Item

To swap the subscription item within the same subscription type "default" to a new price, you can use the following code:

```java

  Subscription stripeSubscription = userStripe.subscription()
                                               .swapItemSubscription("default", "price_id");
```

#### Cancel Subscription

To cancel a subscription at the end of the period, you can use the following code:

```java
userStripe.subscription()
    .cancelAtPeriodEnd("default");
```

#### Trial Subscription

To check if a subscription is on trial, you can use the following code:

```java
userStripe.subscription()
    .onTrial("default");
```

#### Grace Period

To check if a subscription is in the grace period, you can use the following code:

```java
userStripe.subscription()
  .onGracePeriod("default");
```

#### Ended Subscription

To check if a subscription has ended, you can use the following code:

```java
userStripe.subscription()
    .ended();
```

### Subscription Service

To interact with the subscription service, you can use the following code:

```java
@Autowired 
SubscriptionServiceFactory subscriptionServiceFactory;


SubscriptionService subscriptionService = subscriptionServiceFactory.create();

Optional<Subscription> subscription = subscriptionService.getSubscriptionByUserIdAndType("id", "default");
subscription.ifPresent(s -> System.out.println("Subscription found: " + s.getType()));
```

## Contributing

We welcome contributions from everyone. Whether it's a bug report, new feature, correction, or additional documentation, your input is appreciated and valued. Please follow our [contributing guidelines](CONTRIBUTING.md) to help us maintain a high standard of quality.


## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.
