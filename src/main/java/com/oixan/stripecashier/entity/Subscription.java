package com.oixan.stripecashier.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * The {@code Subscription} class represents a subscription entity in the system.
 * It stores details related to a user's subscription, such as the Stripe subscription ID,
 * status, price, and quantities, along with timestamps for creation and updates.
 * 
 * <p>This class is annotated as a JPA entity and is mapped to the "subscriptions" table in the database.
 * It includes lifecycle callbacks to automatically handle the creation and update timestamps.
 * 
 * <p>Indexes are also defined on the {@code user_id} and {@code stripe_status} columns for optimized queries.
 */
@Entity
@Table(name = "subscriptions", indexes = {
        @Index(name = "idx_user_id_stripe_status", columnList = "user_id, stripe_status")
})
public class Subscription {

    /**
     *  
     */
    public Subscription() {
        // Default constructor
    }

    /**
     * The unique identifier for the subscription.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user ID associated with the subscription.
     */
    @Column(name = "user_id", nullable = false)
    private String userId;

    /**
     * The type of the subscription.
     */
    @Column(nullable = false)
    private String type;

    /**
     * The Stripe ID of the subscription.
     */
    @Column(name = "stripe_id", nullable = false, unique = true)
    private String stripeId;

    /**
     * The current status of the subscription on Stripe.
     */
    @Column(name = "stripe_status", nullable = false)
    private String stripeStatus;

    /**
     * The Stripe price ID associated with the subscription.
     */
    @Column(name = "stripe_price")
    private String stripePrice;

    /**
     * The quantity of the subscribed product.
     */
    @Column
    private Long quantity;

    /**
     * The trial expiration date of the subscription.
     */
    @Column(name = "trial_ends_at")
    private LocalDateTime trialEndsAt;

    /**
     * The subscription expiration date.
     */
    @Column(name = "ends_at")
    private LocalDateTime endsAt;

    /**
     * The timestamp of when the subscription was created.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * The timestamp of when the subscription was last updated.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Getters and Setters

    /**
     * Gets the ID of the subscription.
     * 
     * @return The ID of the subscription
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the subscription.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the user ID associated with the subscription.
     * 
     * @return The user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID for the subscription.
     *
     * @param userId the user ID to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the type of the subscription.
     * 
     * @return The type of the subscription
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the subscription.
     *
     * @param type the type of the subscription
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the Stripe ID of the subscription.
     * 
     * @return The Stripe ID
     */
    public String getStripeId() {
        return stripeId;
    }

    /**
     * Sets the Stripe ID for the subscription.
     *
     * @param stripeId the Stripe ID to set
     */
    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    /**
     * Gets the Stripe status of the subscription.
     * 
     * @return The Stripe status
     */
    public String getStripeStatus() {
        return stripeStatus;
    }

    /**
     * Sets the status of the subscription in Stripe.
     *
     * @param stripeStatus the new status of the subscription in Stripe
     */
    public void setStripeStatus(String stripeStatus) {
        this.stripeStatus = stripeStatus;
    }

    /**
     * Gets the Stripe price ID associated with the subscription.
     * 
     * @return The Stripe price ID
     */
    public String getStripePrice() {
        return stripePrice;
    }

    /**
     * Sets the Stripe price for the subscription.
     *
     * @param stripePrice the Stripe price to set
     */
    public void setStripePrice(String stripePrice) {
        this.stripePrice = stripePrice;
    }

    /**
     * Gets the quantity of the subscribed product.
     * 
     * @return The quantity of the product
     */
    public Long getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity for the subscription.
     *
     * @param quantity the quantity to set
     */
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the trial expiration date for the subscription.
     * 
     * @return The trial expiration date
     */
    public LocalDateTime getTrialEndsAt() {
        return trialEndsAt;
    }

    /**
     * Sets the date and time when the trial period ends.
     *
     * @param trialEndsAt the LocalDateTime representing the end of the trial period
     */
    public void setTrialEndsAt(LocalDateTime trialEndsAt) {
        this.trialEndsAt = trialEndsAt;
    }

    /**
     * Gets the expiration date of the subscription.
     * 
     * @return The subscription expiration date
     */
    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    /**
     * Sets the end date and time for the subscription.
     *
     * @param endsAt the LocalDateTime when the subscription ends
     */
    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }

    /**
     * Gets the creation timestamp of the subscription.
     * 
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation date and time of the subscription.
     *
     * @param createdAt the LocalDateTime when the subscription was created
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last update timestamp of the subscription.
     * 
     * @return The last update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the updatedAt timestamp for the subscription.
     *
     * @param updatedAt the LocalDateTime to set as the updatedAt timestamp
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Lifecycle callback method that is triggered before the subscription is persisted to the database.
     * Sets the creation and update timestamps.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Lifecycle callback method that is triggered before the subscription is updated in the database.
     * Sets the update timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
