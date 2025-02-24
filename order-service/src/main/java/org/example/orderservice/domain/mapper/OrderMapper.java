package org.example.orderservice.domain.mapper;

import org.example.orderservice.application.dto.order.OrderRequest;
import org.example.orderservice.application.dto.order.OrderResponse;
import org.example.orderservice.domain.model.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponse toResponse(Order order);
}
