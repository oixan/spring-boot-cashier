package com.oixan.stripecashier.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.oixan.stripecashier.builder.CheckoutBuilder;
import com.oixan.stripecashier.builder.SubscriptionBuilder;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.interfaces.IUserStripeAction;
import com.oixan.stripecashier.manager.SubscriptionManager;

public class UserStripeActionProxy implements InvocationHandler{

	    private final IUserStripe target; 
	    private final CheckoutBuilder checkoutBuilder;
			private final SubscriptionBuilder subscriptionBuilder;
			private final SubscriptionManager subscriptionManager;

	    public UserStripeActionProxy(
	    		IUserStripe target, 
	    		CheckoutBuilder checkoutBuilder,
					SubscriptionBuilder subscriptionBuilder,
					SubscriptionManager subscriptionManager
	    ) {
	        this.target = target;
	        this.checkoutBuilder = checkoutBuilder;
					this.subscriptionBuilder = subscriptionBuilder;
					this.subscriptionManager = subscriptionManager;
	    }

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
	        return method.invoke(target, args); 
	    }

	    public static <T> IUserStripeAction createProxy(
	    		IUserStripe target,
	    		CheckoutBuilder checkoutBuilder,
					SubscriptionBuilder subscriptionBuilder,
					SubscriptionManager subscriptionManager
	    ) {
	        return (IUserStripeAction) Proxy.newProxyInstance(
	            target.getClass().getClassLoader(),
	            new Class[]{IUserStripeAction.class},
	            new UserStripeActionProxy(target, checkoutBuilder, subscriptionBuilder, subscriptionManager)
	        );
	    }
}
