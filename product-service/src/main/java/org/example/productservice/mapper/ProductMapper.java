package org.example.productservice.mapper;

import org.example.productservice.dto.product.ProductRequest;
import org.example.productservice.dto.product.ProductResponse;
import org.example.productservice.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    ProductRequest toDto(Product product);

    Product toEntity(ProductRequest productRequest);

    ProductResponse toResponse(Product product);

}
