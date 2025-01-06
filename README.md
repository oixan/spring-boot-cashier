# Spring-boot-cashier
** WORK IN PROGRESS **

Spring Boot Cashier provides an expressive, fluent interface to Stripe's subscription billing services. It handles almost all of the boilerplate subscription billing code you are dreading writing. In addition to basic subscription management, Spring boot Cashier can handle coupons, swapping subscriptions, subscription "quantities", cancellation grace periods.

## Documentation

## Prerequisites

The `UserEntity` class must implement the `IUserStripe` interface.

### Example

Here is an example of how to add the `IUserStripe` interface to the `UserEntity` class:

```java
public class UserEntity implements IUserStripe {
  // Your implementation here
}
```

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