package org.example.cartservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface ProductClient {

    @GetExchange("/api/categories/{categoryId}/products/{productId}/availability")
    @CircuitBreaker(name = "product", fallbackMethod = "fallbackMethod")
    @Retry(name = "product")
    boolean isProductAvailable(@PathVariable("categoryId") Long categoryId,
                               @PathVariable("productId") Long productId,
                               @RequestParam Integer quantity);

    default boolean fallbackMethod(Long categoryId, Long productId, Integer quantity, Throwable throwable) {
        // TODO add logging

        return false;
    }
}
