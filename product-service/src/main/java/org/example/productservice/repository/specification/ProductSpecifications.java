package org.example.productservice.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.productservice.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecifications {

    private static Specification<Product> hasCategory(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    private static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    private static Specification<Product> minPrice(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) -> {
            Predicate pricePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            return criteriaBuilder.and(pricePredicate);
        };
    }

    private static Specification<Product> maxPrice(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            Predicate pricePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            return criteriaBuilder.and(pricePredicate);
        };
    }

    private Specification<Product> alwaysTrue() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    private Specification<Product> prepareSpecification(Product product) {
        return alwaysTrue();
    }
}
