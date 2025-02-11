package org.example.orderservice.infrastructure.repository;

import org.example.orderservice.domain.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
