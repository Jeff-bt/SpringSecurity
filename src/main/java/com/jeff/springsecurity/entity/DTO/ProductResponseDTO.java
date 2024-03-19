package com.jeff.springsecurity.entity.DTO;

import com.jeff.springsecurity.entity.Product;

public record ProductResponseDTO(String id, String name, Integer price) {
    public ProductResponseDTO(Product product){
        this(product.getId(), product.getName(), product.getPrice());
    }
}
