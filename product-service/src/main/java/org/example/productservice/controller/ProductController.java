package org.example.productservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.productservice.dto.product.ProductRequest;
import org.example.productservice.dto.product.ProductResponse;
import org.example.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
class ProductController {
    private final ProductService productService;

    @PostMapping("/{categoryId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    ProductResponse createProduct(@RequestBody ProductRequest productRequest, @PathVariable Long categoryId) {
        return productService.createProduct(productRequest, categoryId);
    }

    @GetMapping("/{categoryId}/products")
    @ResponseStatus(HttpStatus.OK)
    List<ProductRequest> getAllProducts(@PathVariable Long categoryId) {
        return productService.getAllProducts();
    }

    @GetMapping("/{categoryId}/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    ProductResponse getProductById(@PathVariable Long categoryId, @PathVariable Long productId) {
        return productService.getProductById(productId, categoryId);
    }
}
