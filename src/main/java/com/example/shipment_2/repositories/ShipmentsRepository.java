package com.example.shipment_2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shipment_2.models.Customer;
import com.example.shipment_2.models.Product;
import com.example.shipment_2.models.Shipment;

public interface ShipmentsRepository extends JpaRepository<Shipment, Integer> {
	List<Shipment> findByCustomer(Customer customer);
	
	List<Shipment> findByProduct(Product product);
	
	List<Shipment> findByProductIn(List<Product> products);
	
	void deleteByCustomer(Customer customer);
	
	void deleteByProduct(Product product);
}
