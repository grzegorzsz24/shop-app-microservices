package org.example.productservice.dto.category;

public record CategoryRequest(
        String name,
        String description,
        Long parentCategoryId
) {
}
