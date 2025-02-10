package org.example.productservice.repository;

import org.example.productservice.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll(Specification<Product> spec, Pageable pageable);
    boolean existsByIdAndQuantityIsGreaterThanEqual(Long id, Integer quantity);
}
