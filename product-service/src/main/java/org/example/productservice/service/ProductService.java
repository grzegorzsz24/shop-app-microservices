package org.example.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.productservice.dto.product.ProductRequest;
import org.example.productservice.dto.product.ProductResponse;
import org.example.productservice.exception.ProductNotFound;
import org.example.productservice.mapper.ProductMapper;
import org.example.productservice.model.Category;
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
    private final CategoryService categoryService;

    public ProductResponse createProduct(ProductRequest productRequest, Long categoryId) {
        Category category = categoryService.getCategoryOrThrow(categoryId);
        Product product = productMapper.toEntity(productRequest);
        product.setCategory(category);
        productRepository.save(product);
        log.info("Product created");

        return productMapper.toResponse(product);
    }

    public List<ProductRequest> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    public ProductResponse getProductById(Long productId, Long categoryId) {
        categoryService.doesCategoryExist(categoryId);
        return productRepository.findById(productId)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new ProductNotFound("Product with id " + productId + " not found"));
    }
}
