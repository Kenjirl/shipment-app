package com.example.shipment_2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shipment_2.models.Merchant;

public interface MerchantsRepository extends JpaRepository<Merchant, Integer> {

}
