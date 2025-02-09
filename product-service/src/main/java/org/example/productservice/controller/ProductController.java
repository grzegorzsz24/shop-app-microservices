package org.example.productservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.productservice.dto.product.FilteredProductRequest;
import org.example.productservice.dto.product.ProductRequest;
import org.example.productservice.dto.product.ProductResponse;
import org.example.productservice.service.ProductService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    List<ProductResponse> getAllProducts(
            @PathVariable Long categoryId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String[] sort

    ) {
        FilteredProductRequest request = new FilteredProductRequest(
                categoryId,
                name,
                minPrice,
                maxPrice
        );
        return productService.getAllProducts(request, page, size, sort);
    }

    @GetMapping("/{categoryId}/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    ProductResponse getProductById(@PathVariable Long categoryId, @PathVariable Long productId) {
        return productService.getProductById(productId, categoryId);
    }

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String[].class, new StringTrimmerEditor(true));
    }
}
