package com.leo.inventory_api.repository;

import com.leo.inventory_api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStockLessThanEqual(int limit);

    boolean existsByName(String name);
}
