package org.example.productservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.productservice.dto.category.CategoryRequest;
import org.example.productservice.dto.category.CategoryResponse;
import org.example.productservice.dto.category.CategoryWithSubCategories;
import org.example.productservice.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CategoryResponse createCategory(@RequestBody CategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @GetMapping("/parents")
    @ResponseStatus(HttpStatus.OK)
    List<CategoryResponse> getAllParentCategories() {
        return categoryService.getAllParentCategories();
    }

    @GetMapping("/{id}")
    CategoryWithSubCategories getCategoryWithSubCategories(@PathVariable Long id) {
        return categoryService.getCategoryWithSubCategories(id);
    }
}
