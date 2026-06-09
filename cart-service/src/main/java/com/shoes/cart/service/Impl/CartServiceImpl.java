package com.shoes.cart.service.Impl;

import com.shoes.cart.client.ProductClient;
import com.shoes.cart.config.SecurityUtils;
import com.shoes.cart.dto.ApiResponse;
import com.shoes.cart.dto.Request.CartItemRequest;
import com.shoes.cart.dto.Response.CartResponse;
import com.shoes.cart.dto.Response.ProductResponse;
import com.shoes.cart.entity.Cart;
import com.shoes.cart.entity.CartItem;
import com.shoes.cart.exception.ResourceNotFoundException;
import com.shoes.cart.mapper.CartItemMapper;
import com.shoes.cart.repository.CartRepository;
import com.shoes.cart.service.CartService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    @Autowired
    private ProductClient productClient;
    @Autowired
    private CartItemMapper cartItemMapper;
    @Override
    public Cart getCartById(Long id) {
        return null;
    }

    @Override
    public CartResponse getCartByUserId() {
        Cart cart = cartRepository.findByUserId(SecurityUtils.getCurrentUserId()).orElseThrow(() -> new ResourceNotFoundException("Cart", "user id", SecurityUtils.getCurrentUserId()));
        return cartItemMapper.toResponse(cart);
    }

    @Override
    public CartResponse addCart(CartItemRequest cartItemRequest) {
        Cart cart = getCartEntityByUserId();
        Optional<CartItem> existingCart = cart.getItems().stream().filter(item -> item.getProductId().equals(cartItemRequest.getProductId()) && item.getSizeVn().equals(cartItemRequest.getSizeVn())).findFirst();
        if (existingCart.isPresent()) {
            CartItem cartItem = existingCart.get();
            int newQuantity = cartItem.getQuantity() + 1;
            cartItem.setQuantity(newQuantity);
        } else {
            ResponseEntity<ApiResponse<ProductResponse>> productResponse = productClient.getProductById(cartItemRequest.getProductId());
            if (productResponse.getBody() == null || !productResponse.getBody().getSuccess()) {
                throw new ResourceNotFoundException("Product not found");
            } else {
                ProductResponse productData = productResponse.getBody().getData();
                CartItem newItem = cartItemMapper.toEntity(cartItemRequest, productData);
                cart.addItem(newItem);
            }
        }
        Cart savedCart = cartRepository.save(cart);
        return cartItemMapper.toResponse(savedCart);
    }

    private Cart getCartEntityByUserId() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return cartRepository.findByUserId(currentUserId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(currentUserId);
                    return cartRepository.save(newCart);
                });
    }

    @Override
    @Transactional
    public void removeProductsFromCart(List<Long> purchasedProductIds) {
        Cart cart = getCartEntityByUserId();
        cart.getItems().removeIf(item -> purchasedProductIds.contains(item.getProductId()));
        cartRepository.save(cart);
    }
}
