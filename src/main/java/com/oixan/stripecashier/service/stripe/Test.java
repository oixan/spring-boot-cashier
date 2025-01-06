package com.oixan.stripecashier.service.stripe;

import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import com.oixan.stripecashier.config.AppConfig;
import com.oixan.stripecashier.entity.Subscription;
import com.oixan.stripecashier.factory.CustomerManagerFactory;
import com.oixan.stripecashier.factory.PaymentMethodsManagerFactory;
import com.oixan.stripecashier.factory.SubscriptionServiceFactory;
import com.oixan.stripecashier.factory.UserStripeFactory;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.interfaces.IUserStripeAction;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.service.SubscriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;

@Service
public class Test {

	public void test() throws StripeException {
		UserTest user = new UserTest();
		
		CustomerManager cm = CustomerManagerFactory.create(user);
		String stripe_id = cm.createAsStripeCustomer(null);
		
		PaymentMethod pm = PaymentMethodsManagerFactory.create(cm)
												.addPaymentMethod("id");
		
		IUserStripeAction userStripe = UserStripeFactory.create(user);
		userStripe.checkout()
				.setPriceId("idprice")
				.setQuantity(1)
				.complete();

		userStripe.subscribe()
				.setPriceId("idprice")
				.start();
		
		SubscriptionService subscriptionService = SubscriptionServiceFactory.create();

		Optional<Subscription> subscription = subscriptionService.getSubscriptionByUserIdAndType("id", "default");
		subscription.ifPresent(s -> System.out.println("Subscription found: " + s.getType()));

		userStripe.subscription()
				.cancelAtPeriodEnd("default");
	}
}

class UserTest implements IUserStripe{
	
	public void ciao() {
		
	}

	@Override
	public String getStripeId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStripeId(String stripeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEmail(String email) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPhone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPhone(String phone) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAddress(String address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPreferredLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPreferredLocales(String preferredLocales) {
		// TODO Auto-generated method stub
		
	}
}
