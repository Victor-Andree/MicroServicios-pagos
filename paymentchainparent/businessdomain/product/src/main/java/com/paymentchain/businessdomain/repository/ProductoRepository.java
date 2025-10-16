package com.paymentchain.businessdomain.repository;

import com.paymentchain.businessdomain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Product, Long> {
}
