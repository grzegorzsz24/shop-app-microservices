package org.example.productservice.service;

import lombok.RequiredArgsConstructor;
import org.example.productservice.dto.category.CategoryRequest;
import org.example.productservice.dto.category.CategoryResponse;
import org.example.productservice.dto.category.CategoryWithSubCategories;
import org.example.productservice.exception.CategoryNotFound;
import org.example.productservice.mapper.CategoryMapper;
import org.example.productservice.model.Category;
import org.example.productservice.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        Long parentCategoryId = request.parentCategoryId();
        if (parentCategoryId != null) {
            Category parentCategory = getCategoryOrThrow(parentCategoryId);
            category.setParentCategory(parentCategory);
        }
        categoryRepository.save(category);

        return categoryMapper.toResponse(category);
    }

    public List<CategoryResponse> getAllParentCategories() {
        return categoryRepository.findByParentCategoryIsNull()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryWithSubCategories getCategoryWithSubCategories(Long id) {
        Category category = categoryRepository.findCategoryWithSubCategoriesById(id)
                .orElseThrow(() -> new CategoryNotFound("Category with id " + id + " not found"));

        return categoryMapper.toWithSubCategories(category);
    }

    public Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFound("Category with id " + id + " not found"));
    }

    public boolean doesCategoryExist(Long id) {
        if (categoryRepository.existsById(id) == false) {
            throw new CategoryNotFound("Category with id " + id + " not found");
        }
        return true;
    }
}
