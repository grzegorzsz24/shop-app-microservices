package org.example.productservice.repository;

import org.example.productservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentCategoryIsNull();

    @Query("SELECT c from Category c LEFT JOIN FETCH c.subCategories WHERE c.id = :id")
    Optional<Category> findCategoryWithSubCategoriesById(Long id);
}
