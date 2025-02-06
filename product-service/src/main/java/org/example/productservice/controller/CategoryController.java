package org.example.productservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.productservice.dto.CategoryRequest;
import org.example.productservice.dto.CategoryResponse;
import org.example.productservice.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
