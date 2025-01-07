package com.oixan.stripecashier.entity;

import com.oixan.stripecashier.interfaces.IUserStripe;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_account")
public class UserAccount implements IUserStripe {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @Column(name = "stripe_id", unique = true, length = 50)
  private String stripeId;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "email", unique = true, nullable = false, length = 150)
  private String email;

  @Column(name = "phone", length = 20)
  private String phone;

  @Column(name = "preferred_locales", length = 255)
  private String preferredLocales;

  public UserAccount() {
    // Costruttore vuoto per JPA
  }

  // Getter e Setter

  public Long getId() {
    return id;
  }

  @Override
  public String getStripeId() {
    return stripeId;
  }

  @Override
  public void setStripeId(String stripeId) {
    this.stripeId = stripeId;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getPhone() {
    return phone;
  }

  @Override
  public void setPhone(String phone) {
    this.phone = phone;
  }

  @Override
  public String getPreferredLocales() {
    return preferredLocales;
  }

  @Override
  public void setPreferredLocales(String preferredLocales) {
    this.preferredLocales = preferredLocales;
  }
}
