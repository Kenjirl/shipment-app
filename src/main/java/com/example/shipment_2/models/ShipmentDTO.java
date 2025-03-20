package com.example.shipment_2.models;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.*;

public class ShipmentDTO {
	@NotNull(message = "Customer is required")
    private Customer customerId;

    @NotNull(message = "Product is required")
    private Product productId;

    @Min(1)
    private int productQuantity;

    @Min(0)
	private double shipmentPrice;

    @Min(0)
	private double totalPrice;
    
    @NotEmpty(message = "Status is required")
	private String status;
    
    @NotNull(message = "Delivery date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt;

    @NotNull(message = "Estimated arrival date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime arrivedAt;

    @AssertTrue(message = "Estimated arrival date must be after or on the same day as delivery date")
    public boolean isArrivedAtValid() {
        if (createdAt == null || arrivedAt == null) {
            return true;
        }
        return !arrivedAt.isBefore(createdAt);
    }

	public Customer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(@NotNull(message = "Customer ID is required") Customer customerId) {
		this.customerId = customerId;
	}

	public Product getProductId() {
		return productId;
	}

	public void setProductId(@NotNull(message = "Product ID is required") Product productId) {
		this.productId = productId;
	}

	public int getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	public double getShipmentPrice() {
		return shipmentPrice;
	}

	public void setShipmentPrice(double shipmentPrice) {
		this.shipmentPrice = shipmentPrice;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(@NotNull(message = "Delivery date is required") LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public @NotNull(message = "Estimated arrival date is required") LocalDateTime getArrivedAt() {
		return arrivedAt;
	}

	public void setArrivedAt(@NotNull(message = "Estimated arrival date is required") LocalDateTime arrivedAt) {
		this.arrivedAt = arrivedAt;
	}
}
