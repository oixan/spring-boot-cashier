package com.oixan.stripecashier.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.oixan.stripecashier.builder.CheckoutBuilder;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.interfaces.IUserStripeAction;

public class UserStripeActionProxy implements InvocationHandler{

	    private final IUserStripe target; 
	    private final CheckoutBuilder checkoutBuilder;

	    public UserStripeActionProxy(
	    		IUserStripe target, 
	    		CheckoutBuilder checkoutBuilder
	    ) {
	        this.target = target;
	        this.checkoutBuilder = checkoutBuilder;
	    }

	    @Override
	    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	        if ("checkout".equals(method.getName())) {
	        	return checkoutBuilder.setUser(target);
	        }
	        return method.invoke(target, args); 
	    }

	    public static <T> IUserStripeAction createProxy(
	    		IUserStripe target,
	    		CheckoutBuilder checkoutBuilder
	    ) {
	        return (IUserStripeAction) Proxy.newProxyInstance(
	            target.getClass().getClassLoader(),
	            new Class[]{IUserStripeAction.class},
	            new UserStripeActionProxy(target, checkoutBuilder)
	        );
	    }
}
