package com.example.shipment_2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shipment_2.models.Customer;

public interface CustomersRepository extends JpaRepository<Customer, Integer> {

}
