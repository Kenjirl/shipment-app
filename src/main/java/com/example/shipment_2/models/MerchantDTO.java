package com.example.shipment_2.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class MerchantDTO {
	@NotEmpty(message = "Name is required")
	@Size(min=3, message="Name should be at least 3 characters")
	@Size(max=50, message="Name cannot exceed 50 characters")
	private String name;
	
	@NotEmpty(message = "Phone number is required")
	@Size(min=12, message="Phone number should be at least 12 characters")
	@Size(max=15, message="Phone number cannot exceed 15 characters")
	private String phone;
	
	@NotEmpty(message = "Address is required")
	@Size(min=3, message="Address should be at least 3 characters")
	@Size(max=200, message="Address cannot exceed 200 characters")
	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
