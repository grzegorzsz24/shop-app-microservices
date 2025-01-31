package org.example.orderservice.mapper;

import org.example.orderservice.dto.OrderRequest;
import org.example.orderservice.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    Order toEntity(OrderRequest orderRequest);
}
