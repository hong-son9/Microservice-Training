package com.shoes.cart.mapper;

import com.shoes.cart.dto.request.CartItemRequest;
import com.shoes.cart.dto.response.CartItemResponse;
import com.shoes.cart.dto.response.CartResponse;
import com.shoes.cart.dto.response.ProductResponse;
import com.shoes.cart.entity.Cart;
import com.shoes.cart.entity.CartItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cart", ignore = true)

    @Mapping(source = "request.productId", target = "productId")
    @Mapping(source = "request.sizeVn", target = "sizeVn")

    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.slug", target = "productSlug")
    @Mapping(source = "product.image", target = "productImage")
    @Mapping(source = "product.price", target = "unitPrice")
    CartItem toEntity(CartItemRequest request, ProductResponse product);

    @Mapping(target = "subtotal", expression = "java(cart.calcSubtotal())")
    @Mapping(target = "totalQuantity", expression = "java(cart.totalQuantity())")
    CartResponse toResponse(Cart cart);

    @Mapping(target = "subTotal", expression = "java(item.subTotal())")
    CartItemResponse toItemResponse(CartItem item);
}
