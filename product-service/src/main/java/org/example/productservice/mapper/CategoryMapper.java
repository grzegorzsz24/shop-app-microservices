package org.example.productservice.mapper;

import org.example.productservice.dto.CategoryRequest;
import org.example.productservice.dto.CategoryResponse;
import org.example.productservice.dto.CategoryWithSubCategories;
import org.example.productservice.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    Category toEntity(CategoryRequest categoryRequest);
    CategoryResponse toResponse(Category category);
    CategoryWithSubCategories toWithSubCategories(Category category);

    @AfterMapping
    default void updateSubCategories(Category category, @MappingTarget CategoryWithSubCategories categoryWithSubCategories) {
        if (category.getSubCategories() != null) {
            List<CategoryResponse> subCategories = category.getSubCategories().stream()
                    .map(sub -> CategoryResponse.builder()
                                .id(sub.getId())
                                .name(sub.getName())
                                .build()
                    ).toList();
            categoryWithSubCategories.setSubCategories(subCategories);
        }
    }
}
