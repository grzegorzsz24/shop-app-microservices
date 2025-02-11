package org.example.orderservice.domain.mapper;

import org.example.orderservice.application.dto.OrderRequest;
import org.example.orderservice.application.dto.OrderResponse;
import org.example.orderservice.domain.model.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    Order toEntity(OrderRequest orderRequest);

    OrderResponse toResponse(Order order);
}
