package com.example.shipment_2.models;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductDTO {
	@NotNull(message = "Merchant is required")
	private Merchant merchantId;

	@NotEmpty(message = "Name is required")
	@Size(min=3, message="Name should be at least 3 characters")
	@Size(max=100, message="Name cannot exceed 100 characters")
	private String name;
	
	private MultipartFile image;
	
	@Min(0)
	private double price;
	
	@NotEmpty(message = "Unit is required")
	@Size(min=3, message="Unit should be at least 3 characters")
	@Size(max=20, message="Unit cannot exceed 20 characters")
	private String unit;

	public Merchant getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Merchant merchantId) {
		this.merchantId = merchantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
