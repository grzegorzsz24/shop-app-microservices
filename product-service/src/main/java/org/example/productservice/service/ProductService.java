package org.example.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.productservice.dto.product.FilteredProductRequest;
import org.example.productservice.dto.product.ProductRequest;
import org.example.productservice.dto.product.ProductResponse;
import org.example.productservice.exception.InvalidSortParameterException;
import org.example.productservice.exception.ProductNotFound;
import org.example.productservice.mapper.ProductMapper;
import org.example.productservice.model.Category;
import org.example.productservice.model.Product;
import org.example.productservice.repository.ProductRepository;
import org.example.productservice.repository.specification.ProductSpecifications;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;
    private final MessageSource messageSource;

    private static final List<String> VALID_SORT_FIELDS = List.of("name", "price");

    public ProductResponse createProduct(ProductRequest productRequest, Long categoryId) {
        Category category = categoryService.getCategoryOrThrow(categoryId);
        Product product = productMapper.toEntity(productRequest);
        product.setCategory(category);
        productRepository.save(product);
        log.info("Product created");

        return productMapper.toResponse(product);
    }

    public List<ProductResponse> getAllProducts(FilteredProductRequest request, int page, int size, String[] sort) {
        Sort sortOrder = createSortOrder(sort);
        Pageable pageable = PageRequest.of(page - 1, size, sortOrder);

        return productRepository.findAll(ProductSpecifications.prepareSpecification(request), pageable).stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public ProductResponse getProductById(Long productId, Long categoryId) {
        categoryService.doesCategoryExist(categoryId);
        return productRepository.findById(productId)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new ProductNotFound(getMessage("not.found.product", productId)));
    }

    private Sort createSortOrder(String[] sort) {
        if (sort == null || sort.length == 0) {
            return Sort.unsorted();
        }
        return Arrays.stream(sort)
                .map(sortParam -> {
                    String[] sortArray = sortParam.split(",");
                    if (sortArray.length != 2) {
                        throw new InvalidSortParameterException(getMessage("invalid.sort.parameters",
                                String.join(",", sortArray). replace(" ", "")));
                    }

                    String sortField = sortArray[0].trim();
                    String sortDirection = sortArray[1].trim().toUpperCase();

                    if (!VALID_SORT_FIELDS.contains(sortField)) {
                        throw new InvalidSortParameterException(getMessage("invalid.sort.field", sortField));
                    }

                    Sort.Direction direction;
                    try {
                        direction = Sort.Direction.fromString(sortDirection);
                    } catch (IllegalArgumentException e) {
                        throw new InvalidSortParameterException(getMessage("invalid.sort.direction", sortDirection));
                    }

                    return Sort.by(direction, sortField);
                        }
                ).reduce(Sort::and)
                .orElse(Sort.unsorted());
    }

    private String getMessage(String code, Object... params) {
        return messageSource.getMessage(code, params, Locale.getDefault());
    }
}
