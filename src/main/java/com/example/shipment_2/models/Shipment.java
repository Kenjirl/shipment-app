package com.example.shipment_2.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;

@Entity
@Table(name = "shipments")
public class Shipment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
	
    private int productQuantity;
	private double shipmentPrice;
	private double totalPrice;
	private String status;
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	private LocalDateTime arrivedAt;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
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
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getArrivedAt() {
		return arrivedAt;
	}
	public void setArrivedAt(LocalDateTime arrivedAt) {
		this.arrivedAt = arrivedAt;
	}
}
