package org.example.cartservice.mapper;

import org.example.cartservice.dto.CartItemDto;
import org.example.cartservice.dto.CartResponse;
import org.example.cartservice.model.Cart;
import org.example.cartservice.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartMapper {

    CartResponse toResponse(Cart cart);

    CartItem toItem(CartItemDto cartItemDto);
}
