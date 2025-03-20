package com.example.shipment_2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shipment_2.models.Merchant;
import com.example.shipment_2.models.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer> {
	List<Product> findByMerchant(Merchant merchant);
}
