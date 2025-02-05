package org.example.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.productservice.dto.ProductDto;
import org.example.productservice.exception.ProductNotFound;
import org.example.productservice.mapper.ProductMapper;
import org.example.productservice.model.Product;
import org.example.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductDto createProduct(ProductDto productRequest) {
        Product product = productMapper.toEntity(productRequest);
        productRepository.save(product);
        log.info("Product created");

        return productMapper.toDto(product);
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    public ProductDto getProductById(String id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ProductNotFound("Product with id " + id + " not found"));
    }
}
