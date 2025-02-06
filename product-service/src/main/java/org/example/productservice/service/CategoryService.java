package org.example.productservice.service;

import lombok.RequiredArgsConstructor;
import org.example.productservice.dto.CategoryRequest;
import org.example.productservice.dto.CategoryResponse;
import org.example.productservice.mapper.CategoryMapper;
import org.example.productservice.model.Category;
import org.example.productservice.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        categoryRepository.save(category);

        return categoryMapper.toResponse(category);
    }
}
