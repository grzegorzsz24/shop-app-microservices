package org.example.productservice.service;

import lombok.RequiredArgsConstructor;
import org.example.productservice.dto.category.CategoryRequest;
import org.example.productservice.dto.category.CategoryResponse;
import org.example.productservice.dto.category.CategoryWithSubCategories;
import org.example.productservice.exception.CategoryNotFound;
import org.example.productservice.mapper.CategoryMapper;
import org.example.productservice.model.Category;
import org.example.productservice.repository.CategoryRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final MessageSource messageSource;

    private static final String CATEGORY_NOT_FOUND = "not.found.category";

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
                .orElseThrow(() -> new CategoryNotFound(getMessage(id)));

        return categoryMapper.toWithSubCategories(category);
    }

    public Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFound(getMessage(id)));
    }

    public void doesCategoryExist(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFound(getMessage(id));
        }
    }

    private String getMessage(Object... params) {
        return messageSource.getMessage(CategoryService.CATEGORY_NOT_FOUND, params, Locale.getDefault());
    }

}
