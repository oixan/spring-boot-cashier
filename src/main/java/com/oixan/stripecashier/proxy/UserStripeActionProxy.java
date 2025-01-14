package com.oixan.stripecashier.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.oixan.stripecashier.builder.ChargeBuilder;
import com.oixan.stripecashier.builder.CheckoutBuilder;
import com.oixan.stripecashier.builder.SubscriptionBuilder;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.interfaces.IUserStripeAction;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;
import com.oixan.stripecashier.manager.SubscriptionManager;


/**
 * A proxy implementation for delegating actions to the appropriate builder or manager 
 * associated with a Stripe user.
 */
public class UserStripeActionProxy implements InvocationHandler {

    private final IUserStripe target; 
    private final CheckoutBuilder checkoutBuilder;
    private final SubscriptionBuilder subscriptionBuilder;
    private final SubscriptionManager subscriptionManager;
    private final CustomerManager customerManager;
    private final PaymentMethodsManager paymentMethodsManager;
    private final ChargeBuilder chargeBuilder;

    /**
     * Constructs a new UserStripeActionProxy.
     *
     * @param target the IUserStripe instance to be proxied.
     * @param checkoutBuilder the builder for handling checkout operations.
     * @param subscriptionBuilder the builder for managing subscriptions.
     * @param subscriptionManager the manager for handling subscription actions.
     * @param customerManager the manager for handling customer-related operations.
     * @param paymentMethodsManager the manager for managing payment methods.
     */
    public UserStripeActionProxy(
            IUserStripe target, 
            CheckoutBuilder checkoutBuilder,
            SubscriptionBuilder subscriptionBuilder,
            SubscriptionManager subscriptionManager,
            CustomerManager customerManager,
            PaymentMethodsManager paymentMethodsManager,
            ChargeBuilder chargeBuilder
    ) {
        this.target = target;
        this.checkoutBuilder = checkoutBuilder;
        this.subscriptionBuilder = subscriptionBuilder;
        this.subscriptionManager = subscriptionManager;
        this.customerManager = customerManager;
        this.paymentMethodsManager = paymentMethodsManager;
        this.chargeBuilder = chargeBuilder;
    }

    /**
     * Intercepts method calls on the proxied object and delegates them to the appropriate
     * builder, manager, or the target object.
     *
     * @param proxy the proxy instance that the method was invoked on.
     * @param method the method being called.
     * @param args the arguments passed to the method.
     * @return the result of the method invocation.
     * @throws Throwable if an error occurs during method invocation.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("checkout".equals(method.getName())) {
            return checkoutBuilder.setUser(target);
        }
        if ("subscribe".equals(method.getName())) {
            return subscriptionBuilder;
        }
        if ("getUserStripe".equals(method.getName())) {
            return target;
        }
        if ("subscription".equals(method.getName())) {
            return subscriptionManager;
        }
        if ("customer".equals(method.getName())) {
            return customerManager;
        }
        if ("paymentMethod".equals(method.getName())) {
            return paymentMethodsManager;
        }
        if ("charge".equals(method.getName()))
        {
            return chargeBuilder;
        }
        return method.invoke(target, args); 
    }

    /**
     * Creates a proxy instance for IUserStripeAction.
     *
     * @param target the IUserStripe instance to be proxied.
     * @param checkoutBuilder the builder for handling checkout operations.
     * @param subscriptionBuilder the builder for managing subscriptions.
     * @param subscriptionManager the manager for handling subscription actions.
     * @param customerManager the manager for handling customer-related operations.
     * @param paymentMethodsManager the manager for managing payment methods.
     * @param <T> the type of IUserStripeAction.
     * @return a new IUserStripeAction proxy instance.
     */
    public static <T> IUserStripeAction createProxy(
            IUserStripe target,
            CheckoutBuilder checkoutBuilder,
            SubscriptionBuilder subscriptionBuilder,
            SubscriptionManager subscriptionManager,
            CustomerManager customerManager,
            PaymentMethodsManager paymentMethodsManager,
            ChargeBuilder chargeBuilder
    ) {
        return (IUserStripeAction) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            new Class[]{IUserStripeAction.class},
            new UserStripeActionProxy(
                target, checkoutBuilder, subscriptionBuilder, subscriptionManager, customerManager, paymentMethodsManager, chargeBuilder)
        );
    }
}

