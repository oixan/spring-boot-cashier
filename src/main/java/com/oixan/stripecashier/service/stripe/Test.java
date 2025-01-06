package com.oixan.stripecashier.service.stripe;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.oixan.stripecashier.entity.Subscription;
import com.oixan.stripecashier.factory.SubscriptionServiceFactory;
import com.oixan.stripecashier.factory.UserStripeFactory;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.interfaces.IUserStripeAction;
import com.oixan.stripecashier.service.SubscriptionService;

import com.stripe.exception.StripeException;

@Service
public class Test {

	public void test() throws StripeException {
		UserTest user = new UserTest();
		IUserStripeAction userStripe = UserStripeFactory.create(user);

		userStripe.customer()
						.createAsStripeCustomer(null);

		userStripe.paymentMethod()
						.addPaymentMethod("id");				

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
