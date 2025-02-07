package org.example.productservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CategoryWithSubCategories {
    Long id;
    String name;
    String description;
    List<CategoryResponse> subCategories;
}
