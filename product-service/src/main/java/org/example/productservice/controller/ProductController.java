package org.example.productservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.productservice.dto.ProductDto;
import org.example.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
class ProductController {
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ProductDto createProduct(@RequestBody ProductDto productRequest) {
        return productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }
}
