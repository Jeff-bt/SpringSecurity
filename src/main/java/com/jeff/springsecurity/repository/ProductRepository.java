package com.jeff.springsecurity.repository;

import com.jeff.springsecurity.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
