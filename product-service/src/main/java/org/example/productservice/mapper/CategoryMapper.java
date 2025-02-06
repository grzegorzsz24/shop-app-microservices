package org.example.productservice.mapper;

import org.example.productservice.dto.CategoryRequest;
import org.example.productservice.dto.CategoryResponse;
import org.example.productservice.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    Category toEntity(CategoryRequest categoryRequest);
    CategoryResponse toResponse(Category category);
}
